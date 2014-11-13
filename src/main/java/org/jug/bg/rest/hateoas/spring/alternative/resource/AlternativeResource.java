/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.alternative.resource;

import org.jug.bg.rest.hateoas.spring.alternative.payload.AlternativePayload;
import org.jug.bg.rest.hateoas.spring.alternative.repository.Alternative;
import org.jug.bg.rest.hateoas.spring.alternative.repository.AlternativeParameter;
import org.jug.bg.rest.hateoas.spring.alternative.repository.AlternativeRepository;
import org.jug.bg.rest.hateoas.spring.common.resource.AbstractResource;
import org.jug.bg.rest.hateoas.spring.common.resource.BadRequestException;
import org.jug.bg.rest.hateoas.spring.common.resource.NotFoundException;
import org.jug.bg.rest.hateoas.spring.poll.resource.PollResource;
import org.jug.bg.rest.hateoas.spring.vote.repository.VoteParameter;
import org.jug.bg.rest.hateoas.spring.vote.resource.VoteResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A class representing an alternative resource.
 *
 * @author Nikolay Vasilev
 */
@RestController // indicates that class will handle requests for alternative resource
@ExposesResourceFor(AlternativePayload.class) // used by EntityLinks accessor to look up resources
@RequestMapping("/polls/{pollId}/alternatives")
public class AlternativeResource extends AbstractResource {

    @Autowired
    private AlternativeRepository repository;

    @Autowired
    private VoteResource votesResource;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_HAL_JSON)
    public ResponseEntity<List<AlternativePayload>> getAlternatives(
        @PathVariable("pollId") Long pollId)
    {
        // prepare parameter object which is used as a DTO to transfer data across the various layers of the application
        AlternativeParameter parameter = AlternativeParameter.Builder.builder().withPollId(pollId).build();

        // retrieve all alternatives for the given poll id
        List<Alternative> alternatives = repository.retrieveAllAlternatives(parameter);

        // build the payload
        // FIXME: move this logic to a dedicated factory method
        List<AlternativePayload> alternativesPayload = new ArrayList<>();
        for (Alternative alternative: alternatives) {
            AlternativePayload alternativePayload = buildPayload(parameter, alternative);
            alternativesPayload.add(alternativePayload);
        }

        return new ResponseEntity<>(alternativesPayload, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_HAL_JSON)
    public ResponseEntity<AlternativePayload> getAlternative(
        @PathVariable("pollId") Long pollId,
        @PathVariable("id")     Long alternativeId)
    {
        AlternativeParameter parameter = AlternativeParameter.Builder.builder()
                            .withPollId(pollId).withAlternativeId(alternativeId).build();

        Alternative alternative = repository.retrieveAlternative(parameter);

        if (alternative == null) {
            // there is a method handling this situation in the parent class
            throw new NotFoundException(
                "Missing alternative with (pollId, attrId): (" + pollId + ", " + alternativeId + ")");
        }

        // one way to create response's payload is using an assembler
        AlternativePayload payload = buildPayload(parameter, alternative);

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = APPLICATION_HAL_JSON)
    public ResponseEntity<AlternativePayload> createAlternative(
        @PathVariable("pollId") Long pollId,
        @RequestBody AlternativeParameter requestPayload)
    {
        String value = requestPayload.getValue();
        if (value == null || value.isEmpty()) {
            // this exceptional situation is handled by the parent's handler method
            throw new BadRequestException("Invalid alternative value.");
        }
        AlternativeParameter parameter = AlternativeParameter.Builder.builder()
                            .withPollId(pollId).withValue(value).build();
        Alternative alternative = repository.storeAlternative(parameter);
        AlternativePayload payload = buildPayload(parameter, alternative);
        return new ResponseEntity<>(payload, HttpStatus.CREATED);
    }

    // FIXME: move away this logic into a dedicated @Service or @Component
    private AlternativePayload buildPayload(AlternativeParameter parameter, Alternative alternative) {
        Long pollId = parameter.getPollId();
        Long alternativeId = alternative.getAlternativeId();

        // delegating to the VotesResource to calculate the votes count
        Integer votesCount = votesResource.getVoteCounts(pollId, alternativeId).getBody();

        // building the state part of the HAL response
        AlternativePayload alternativePayload = AlternativePayload.Builder.builder()
                .withId(alternativeId).withValue(alternative.getValue()).withVotesCount(votesCount)
                .build();

        // adding links to the HAL response
        addLinks(parameter, alternativePayload);

        // as there is no sense to add list of votes associated to this alternative (the voting must be anonimous),
        // there is no need to embed resources

        return alternativePayload;
    }

    /**
     * Auxiliary method for adding links to the payload.
     *
     * <p><strong>Note:</strong> Note that this is part of the resource's payload construction and if we follow the best
     * practices, the resource (in the terms of MVC is the controller), should be thin and delegate the real work to
     * services and other components etc.
     * <br />
     * <br />
     * The issue for such solution here is that the {@link Link} objects could be considered as part of the
     * (RESTFul) representation of the resource. That is the reason to be built as part of the resource (rest
     * controller).
     * <br />
     * <br />
     * A compromise would probably be to create a <code>LinkBuilder</code> class dedicated on building Links. One could
     * think that perhaps adding the links could be done in an assembler class, but then there is cyclic dependency
     * between assembler's and resource's (i.e. ex-controller's) packages, because the assembler will need the resource
     * for building the link and the resource itself will call the assembler in order to build the payload.</p>
     *
     * @param parameter Alternative parameter.
     * @param payload Alternative payload
     */
    private void addLinks(AlternativeParameter parameter, AlternativePayload payload) {
        Link selfLink = aSelfLink(parameter, payload);
        Link votesCountLink = aVotesCountLink(parameter, payload);
        Link allAlternativesLink = allAlternativesLink(parameter);
        Link pollLink = aPollLink(parameter);

        // Note 1:
        // Note that we do NOT declare the following add(...) method to our payload class in order to add links
        // it's inherited by ResourceSupport class
        //
        // Note 2:
        // Mind that the order of the links defined here is of importance when serialized later on.
        payload.add(selfLink);
        payload.add(votesCountLink);
        payload.add(allAlternativesLink);
        payload.add(pollLink);
    }

    /**
     * Builder method for selfLink to resource using {@link ControllerLinkBuilder} class.
     *
     * @param voteParameter Vote parameter.
     * @param payload       Vote resource payload.
     *
     *  @return a self link to a resource.
     */
    private Link aSelfLink(AlternativeParameter voteParameter, AlternativePayload payload) {
        long pollId = voteParameter.getPollId();
        long alternativeId = payload.getAlternativeId();

        return ControllerLinkBuilder.linkTo(

                    // getting url mapping from controller's clas
                    ControllerLinkBuilder.methodOn(AlternativeResource.class)

                            // url mapping from the corresponding handling method
                            .getAlternative(pollId, alternativeId))

                .withSelfRel(); // adding the self "rel" attribute to the link
    }

    /**
     * Builder method for a link to votes count resource using {@link ControllerLinkBuilder} class.
     *
     * @param alternativeParameter Alternative parameter.
     * @param payload              Alternative payload.
     *
     * @return a link to votes count resource.
     */
    private Link aVotesCountLink(AlternativeParameter alternativeParameter, AlternativePayload payload) {
        long pollId = alternativeParameter.getPollId();
        long alternativeId = payload.getAlternativeId(); // dirty hack but I don't hive time to refactor the code right now

        return ControllerLinkBuilder.linkTo(

                    // getting url mapping from controller's class
                    ControllerLinkBuilder.methodOn(VoteResource.class)

                        // url mapping from the corresponding handling method
                        .getVoteCounts(pollId, alternativeId))

                .withRel("votesCount"); // adding a customer-specific "rel" attribute to the link
    }

    /**
     * Builder method for a link to all alternatives for the given poll.
     *
     * @param parameter Alternative parameter.
     *
     * @return a link to all alternatives for the given poll.
     */
    private Link allAlternativesLink(AlternativeParameter parameter) {
        long pollId = parameter.getPollId();
        return linkTo(methodOn(AlternativeResource.class).getAlternatives(pollId)).withRel("alternatives");
    }

    /**
     * Builder method for a link to all alternatives for the given poll.
     *
     * @param parameter Alternative parameter.
     *
     * @return a link to all alternatives for the given poll.
     */
    private Link aPollLink(AlternativeParameter parameter) {
        long pollId = parameter.getPollId();
        return linkTo(methodOn(PollResource.class).getPoll(pollId)).withRel("poll");
    }
}
