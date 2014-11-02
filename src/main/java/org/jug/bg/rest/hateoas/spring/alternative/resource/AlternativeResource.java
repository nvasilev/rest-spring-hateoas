package org.jug.bg.rest.hateoas.spring.alternative.resource;

import org.jug.bg.rest.hateoas.spring.alternative.payload.AlternativePayload;
import org.jug.bg.rest.hateoas.spring.alternative.repository.AlternativeData;
import org.jug.bg.rest.hateoas.spring.alternative.repository.AlternativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/polls/1/alternatives")
public class AlternativeResource {

    @Autowired
    private AlternativeRepository repository;

    @RequestMapping(value = "/{id}", method= RequestMethod.GET)
    public HttpEntity<AlternativePayload> getAlternative(@PathVariable("id") Long alternativeId) {
        AlternativeData alternativeData = repository.find(alternativeId);
        AlternativePayload alternativePayload = buildAlternative(alternativeData);
        return new ResponseEntity<>(alternativePayload, HttpStatus.OK);
    }

    private AlternativePayload buildAlternative(AlternativeData data) {
        return AlternativePayload.Builder.builder().withId(data.getAlternativeId()).withValue(data.getValue()).build();
    }
}
