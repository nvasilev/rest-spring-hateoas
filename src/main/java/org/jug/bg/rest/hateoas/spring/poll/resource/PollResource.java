/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.poll.resource;

import org.jug.bg.rest.hateoas.spring.alternative.payload.AlternativePayload;
import org.jug.bg.rest.hateoas.spring.alternative.resource.AlternativeResource;
import org.jug.bg.rest.hateoas.spring.common.resource.AbstractResource;
import org.jug.bg.rest.hateoas.spring.common.resource.BadRequestException;
import org.jug.bg.rest.hateoas.spring.poll.payload.PollPayload;
import org.jug.bg.rest.hateoas.spring.poll.repository.Poll;
import org.jug.bg.rest.hateoas.spring.poll.repository.PollParameter;
import org.jug.bg.rest.hateoas.spring.poll.repository.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * A class representing a poll resource.
 *
 * @author Nikolay Vasilev
 */
@RestController
@ExposesResourceFor(PollPayload.class) // used by EntityLinks accessor to look up resources
@RequestMapping("/polls")
public class PollResource extends AbstractResource {

    @Autowired
    private PollRepository repository;

    @Autowired
    private AlternativeResource alternativeResource;

    // used for generating links to resources (classes annotated with @ExposesResourceFor)
    @Autowired
    private EntityLinks entityLinks;

    @RequestMapping(value = "/{id}",
                    method = GET,
                    produces = APPLICATION_HAL_JSON)
    public ResponseEntity<PollPayload> getPoll(@PathVariable("id") Long pollId) {
        PollParameter parameter = PollParameter.Builder.builder().withPollId(pollId).build();

        Poll data = repository.retrievePoll(parameter);

        PollPayload payload = buildPayload(parameter, data);

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @RequestMapping(method = POST, produces = APPLICATION_HAL_JSON)
    public ResponseEntity<PollPayload> createPoll(
        @RequestBody PollParameter requestPayload) {

        String topic = requestPayload.getTopic();

        if (topic == null || topic.isEmpty()) {
            throw new BadRequestException("Missing poll topic.");
        }

        PollParameter parameter = PollParameter.Builder.builder().withTopic(topic).build();

        Poll data = repository.storePoll(parameter);

        PollPayload payload = buildPayload(parameter, data);

        return new ResponseEntity<>(payload, HttpStatus.CREATED);
    }

    @RequestMapping(value="{id}", method = DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePoll(@PathVariable Long id) {
        repository.removePoll(id);
    }

    // FIXME: move this method into a dedicated @Service or @Component
    private PollPayload buildPayload(PollParameter parameter, Poll data) {

        // delegating the building of the embedded resources to the corresponding resource (rest controller) instead
        // of building them on our own
        List<AlternativePayload> embeddedResources = alternativeResource.getAlternatives(data.getId()).getBody();

        List<Link> links = buildLinks(data, embeddedResources);

        return PollPayload.Builder.builder().withPollData(data).withAlternatives(embeddedResources).withLinks(links)
                                  .build();
    }

    /**
     * Builds all links which are to be included in the serialized version of the resource.
     *
     * @param data Data to be used for link creation.
     * @param embeddedResources Embedded resources for which links will be built.
     *
     * @return all links which are to be included in the serialized version of the resource.
     */
    private List<Link> buildLinks(Poll data, List<AlternativePayload> embeddedResources) {
        Link aSelfLink = aSelfLink(data);
        List<Link> linksToEmbeddedResources = buildLinksToEmbeddedResources(embeddedResources);

        List<Link> links = new ArrayList<>();
        links.add(aSelfLink);
        links.addAll(linksToEmbeddedResources);

        return links;
    }

    /**
     * Building self link.
     * @param data Data to be used for self link creation.
     * @return self link.
     */
    private Link aSelfLink(Poll data) {
        // employing the framework, via EntityLink class to build the link on our behalf
        return entityLinks.linkToSingleResource(PollPayload.class, data.getId());
    }

    /**
     * Builds links to all embedded resources.
     *
     * @param embeddedResources Embedded resources for which links will be built.
     *
     * @return List of links to embedded resources. If no resources are provided the result is an empty list.
     */
    private List<Link> buildLinksToEmbeddedResources(List<AlternativePayload> embeddedResources) {
        if (embeddedResources.isEmpty()){
            return Collections.emptyList();
        }
        List<Link> linksList = new ArrayList<>();

        // what is to be done is using the self link of the embedded resources and building links to them.
        for (AlternativePayload alternative : embeddedResources) {

            // Note that withRel(...) method is a factory method! I.e. we do not modify alternative's original self link
            Link alternativeLink = alternative.getLink(Link.REL_SELF).withRel("alternatives");

            linksList.add(alternativeLink);
        }
        return linksList;
    }
}
