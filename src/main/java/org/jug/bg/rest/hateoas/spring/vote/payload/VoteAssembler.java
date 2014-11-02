package org.jug.bg.rest.hateoas.spring.vote.payload;

import org.jug.bg.rest.hateoas.spring.vote.repository.VoteData;
import org.jug.bg.rest.hateoas.spring.vote.resource.VoteResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class VoteAssembler extends ResourceAssemblerSupport<VoteData, VotePayload> {

    public VoteAssembler() {
        super(VoteResource.class, VotePayload.class);
    }

    @Override
    public VotePayload toResource(VoteData entity) {
        return VotePayload.Builder.builder().withId(entity.getId()).withEmail(entity.getEmail()).build();
    }
}
