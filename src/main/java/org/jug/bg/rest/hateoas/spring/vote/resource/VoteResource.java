/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.vote.resource;

import org.jug.bg.rest.hateoas.spring.common.resource.AbstractResource;
import org.jug.bg.rest.hateoas.spring.common.resource.BadRequestException;
import org.jug.bg.rest.hateoas.spring.common.resource.NotFoundException;
import org.jug.bg.rest.hateoas.spring.vote.payload.VoteAssembler;
import org.jug.bg.rest.hateoas.spring.vote.payload.VotePayload;
import org.jug.bg.rest.hateoas.spring.vote.repository.Vote;
import org.jug.bg.rest.hateoas.spring.vote.repository.VoteParameter;
import org.jug.bg.rest.hateoas.spring.vote.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A class representing vote resource.
 *
 * @author Nikolay Vasilev
 */
@RestController // marking that this class will handle requests for vote resource
@ExposesResourceFor(VotePayload.class) // used by EntityLinks accessor to look up resources
@RequestMapping("/polls/{pollId}/alternatives/{alternativeId}/votes")
public class VoteResource extends AbstractResource {

    @Autowired
    private VoteRepository repository;

    @Autowired
    private VoteAssembler assembler;

    /**
     * Retrieves a vote for provided poll, alternative and vote id.
     * <br />
     * Note: There is no too much sense of this method. The only aim is to serve as an example how a resource without
     * embedded resources is created.
     *
     * @param pollId        Poll id.
     * @param alternativeId Alternative id.
     * @param voteId        Vote id.
     *
     * @return Returns a vote for provided poll, alternative and vote id.
     */
    @RequestMapping(value = "/{id}",
                    method = RequestMethod.GET,
                    produces = APPLICATION_HAL_JSON)
    public ResponseEntity<VotePayload> getVote(
        @PathVariable("pollId") Long pollId,
        @PathVariable("alternativeId") Long alternativeId,
        @PathVariable("id") Long voteId)
    {
        VoteParameter voteParameter = VoteParameter.Builder.builder().withPollId(pollId)
                                                           .withAlternativeId(alternativeId).withVoteId(voteId).build();
        Vote vote = repository.retrieveVote(voteParameter);

        if (vote == null) {
            // there is a method handling this situation in the parent class
            throw new NotFoundException(
                "Missing vote with (pollId, attrId, voteId): (" + pollId + ", " + alternativeId + ", " + voteId + ")");
        }

        // one way to create response's payload is using an assembler
        VotePayload payload = assembler.toResource(vote);

        // no embedded resources are required for the creation, therefore only links are added to the payload
        addLinks(voteParameter, payload);

        // wrapping the payload to a response entity object
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = APPLICATION_HAL_JSON)
    public ResponseEntity<VotePayload> createVote(
        @PathVariable("pollId") Long pollId,
        @PathVariable("alternativeId") Long alternativeId,
        @RequestBody VoteParameter requestPayload) {

        String email = requestPayload.getEmail();
        if (email == null || email.isEmpty()) {
            // this exceptional situation is handled by the parent's handler method
            throw new BadRequestException("Invalid email.");
        }

        VoteParameter voteParameter = VoteParameter.Builder.builder().withPollId(pollId)
                                             .withAlternativeId(alternativeId).withEmail(email).build();

        // creates a vote domain object
        Vote newVote = repository.createVote(voteParameter);

        // converting the domain object to its rest counterpart
        VotePayload payload = assembler.toResource(newVote);

        // as this is a simple rest response (no embedded resources) we add just links to the payload
        addLinks(voteParameter, payload);

        // response code 201, right? ;)
        return new ResponseEntity<>(payload, HttpStatus.CREATED);
    }

    /**
     * Returns votes count for given poll and alternative.
     *
     * @param pollId        Poll id.
     * @param alternativeId Alternative id.
     *
     * @return Returns votes count for given poll and alternative.
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET, produces = APPLICATION_HAL_JSON)
    public ResponseEntity<Integer> getVoteCounts(
        @PathVariable("pollId") Long pollId,
        @PathVariable("alternativeId") Long alternativeId)
    {
        VoteParameter voteParameter = VoteParameter.Builder.builder().withPollId(pollId).withAlternativeId(alternativeId).build();
        List<Vote> votes = repository.retrieveAllVotesForAlternativeAndPoll(voteParameter);
        int votesCount = (votes == null) ? 0 : votes.size();
        return new ResponseEntity<>(votesCount, HttpStatus.OK);
    }

    /**
     * Auxiliary method adding links to the payload.
     *
     * @param voteParameter Vote parameter.
     * @param payload Vote resource payload.
     */
    private void addLinks(VoteParameter voteParameter, VotePayload payload) {

        // building self link using the access to the rest controller request mapping
        Link selfLink = aSelfLink(voteParameter, payload);

        // building a link to the vote counts for the current alternative
        Link votesCountLink = aVotesCountLink(voteParameter);

        // Note that we have NOT declared the add(...) method to our payload class
        // it's inherited by ResourceSupport class
        payload.add(selfLink);
        payload.add(votesCountLink);
    }

    /**
     * Builder method for selfLink to resource using {@link ControllerLinkBuilder} class.
     *
     * @param voteParameter Vote parameter.
     * @param payload       Vote resource payload.
     *
     *  @return a self link to a resource.
     */
    private Link aSelfLink(VoteParameter voteParameter, VotePayload payload) {
        long pollId = voteParameter.getPollId();
        long alternativeId = voteParameter.getAlternativeId();
        long voteId = payload.getVoteId(); // dirty hack as I don't hive time to refactor the code

        return ControllerLinkBuilder.linkTo(

                        // getting url mapping from controller's clas
                        ControllerLinkBuilder.methodOn(VoteResource.class)

                            // url mapping from the corresponding handling method
                            .getVote(pollId, alternativeId, voteId))

                .withSelfRel(); // adding the self "rel" attribute to the link
    }

    /**
     * Builder method for a link to votes count resource using {@link ControllerLinkBuilder} class.
     *
     * @param voteParameter Vote parameter.
     *
     * @return a self link to a resource.
     */
    private Link aVotesCountLink(VoteParameter voteParameter) {
        long pollId = voteParameter.getPollId();
        long alternativeId = voteParameter.getAlternativeId();

        return ControllerLinkBuilder.linkTo(

                        // getting url mapping from controller's class
                        ControllerLinkBuilder.methodOn(VoteResource.class)

                            // url mapping from the corresponding handling method
                            .getVoteCounts(pollId, alternativeId))

                .withRel("votesCount"); // adding a customer-specific "rel" attribute to the link
    }
}
