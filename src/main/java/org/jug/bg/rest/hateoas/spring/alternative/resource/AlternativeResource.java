package org.jug.bg.rest.hateoas.spring.alternative.resource;

import org.jug.bg.rest.hateoas.spring.alternative.payload.AlternativePayload;
import org.jug.bg.rest.hateoas.spring.alternative.repository.AlternativeData;
import org.jug.bg.rest.hateoas.spring.alternative.repository.AlternativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@ExposesResourceFor(AlternativePayload.class)
@RequestMapping("/polls/{pollId}/alternatives")
public class AlternativeResource {

    private static final String APPLICATION_HAL_JSON = "application/hal+json";

    @Autowired
    private AlternativeRepository repository;

    @Autowired
    private EntityLinks entityLinks;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_HAL_JSON)
    public ResponseEntity<List<AlternativePayload>> getAlternatives(
        @PathVariable("pollId")
        Long pollId) {
        List<AlternativePayload> alternatives = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            alternatives.add(buildPayload(pollId, mockData()));
        }
        return new ResponseEntity<>(alternatives, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_HAL_JSON)
    public HttpEntity<AlternativePayload> getAlternative(
        @PathVariable("pollId")
        Long pollId,
        @PathVariable("id")
        Long alternativeId) {
//        AlternativeData data = repository.find(alternativeId);
        AlternativeData data = mockData();
        AlternativePayload alternativePayload = buildPayload(pollId, data);
        return new ResponseEntity<>(alternativePayload, HttpStatus.OK);
    }

    private static long cntr = 1L;

    private AlternativeData mockData() {
        long id = cntr++;
        AlternativeData data = new AlternativeData(id, "alternative " + id);
        return data;
    }


    private AlternativePayload buildPayload(Long pollId, AlternativeData data) {
        AlternativePayload alternative = AlternativePayload.Builder.builder().withId(data.getAlternativeId()).withValue(data.getValue()).build();
        Link selfRel =
            ControllerLinkBuilder.linkTo(
                (ControllerLinkBuilder.methodOn(AlternativeResource.class)
                        .getAlternative(pollId, data.getAlternativeId())))
                                 .withSelfRel();
        alternative.add(selfRel);
        return alternative;
    }
}
