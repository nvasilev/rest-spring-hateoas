/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.poll.resource;

import org.jug.bg.rest.hateoas.spring.alternative.payload.AlternativePayload;
import org.jug.bg.rest.hateoas.spring.alternative.resource.AlternativeResource;
import org.jug.bg.rest.hateoas.spring.poll.payload.PollPayload;
import org.jug.bg.rest.hateoas.spring.poll.repository.PollData;
import org.jug.bg.rest.hateoas.spring.poll.repository.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@ExposesResourceFor(PollPayload.class) // used by EntityLinks accessor to look up resources
@RequestMapping("/polls")
public class PollResource {

    // FIXME: refactor to a common constant/enum
    private static final String APPLICATION_HAL_JSON = "application/hal+json";

    @Autowired
    private PollRepository repository;

    @Autowired
    private AlternativeResource alternativeResource;

    // used for generating links to resources (classes annotated with @ExposesResourceFor)
    @Autowired
    private EntityLinks entityLinks;

    @RequestMapping(value = "/{id}", method = GET, produces = APPLICATION_HAL_JSON)
    public ResponseEntity<PollPayload> getPoll(@PathVariable("id") Long pollId) {

        PollData data = repository.findPoll(pollId);

        PollPayload payload = buildPayload(data);

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @RequestMapping(method = POST, produces = APPLICATION_HAL_JSON)
    public ResponseEntity<PollPayload> createPoll(@RequestBody PollRequestBody requestBody) {

        PollData data = repository.storePoll(requestBody.getTopic());

        PollPayload payload = buildPayload(data);

        return new ResponseEntity<>(payload, HttpStatus.CREATED);
    }

    @RequestMapping(value="{id}", method = DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePoll(@PathVariable Long id) {
        repository.removePoll(id);
    }

    private PollPayload buildPayload(PollData data) {

        List<AlternativePayload> relatedResources = alternativeResource.getAlternatives(data.getId()).getBody();

        List<Link> links = buildLinks(data, relatedResources);

        return PollPayload.Builder.builder().withPollData(data).withAlternatives(relatedResources).withLinks(links)
                                  .build();
    }

    private List<Link> buildLinks(PollData data, List<AlternativePayload> relatedResources) {

        Link selfLink = entityLinks.linkToSingleResource(PollPayload.class, data.getId());

        List<Link> links = new ArrayList<>();

        links.add(selfLink);

        if (!relatedResources.isEmpty()){
            List<Link> relatedResourceLinks = getLinksToRelatedResources(relatedResources);
            links.addAll(relatedResourceLinks);
        }

        return links;
    }

    private List<Link> getLinksToRelatedResources(Iterable<AlternativePayload> relatedResources) {
        List<Link> linksList = new ArrayList<>();

        for (AlternativePayload alternative : relatedResources) {
            Link alternativeLink = alternative.getLink(Link.REL_SELF).withRel("alternatives");
            linksList.add(alternativeLink);
        }

        return linksList;
    }
}
