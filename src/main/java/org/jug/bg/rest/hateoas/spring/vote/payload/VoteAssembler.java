/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.vote.payload;

import org.jug.bg.rest.hateoas.spring.vote.repository.Vote;
import org.jug.bg.rest.hateoas.spring.vote.resource.VoteResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * Dedicated class for mapping a domain object to its corresponding rest response payload representation.
 *
 * @author Nikolay Vasilev
 */
@Component
public class VoteAssembler extends ResourceAssemblerSupport<Vote, VotePayload> {

    public VoteAssembler() {
        super(VoteResource.class, VotePayload.class);
    }

    // converts the domain object into a resource payload
    @Override
    public VotePayload toResource(Vote entity) {
        VotePayload payload = instantiateResource(entity);
        return payload;
    }

    // Provides a default implementation if no default constructor is provided for the payload
    @Override
    protected VotePayload instantiateResource(Vote entity) {
        return VotePayload.Builder.builder().withId(entity.getId()).withEmail(entity.getEmail()).build();
    }
}
