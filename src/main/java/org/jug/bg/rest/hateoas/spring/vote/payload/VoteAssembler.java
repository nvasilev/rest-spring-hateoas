package org.jug.bg.rest.hateoas.spring.vote.payload;

import org.jug.bg.rest.hateoas.spring.alternative.payload.AlternativePayload;
import org.jug.bg.rest.hateoas.spring.vote.repository.VoteData;
import org.jug.bg.rest.hateoas.spring.vote.resource.VoteResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class VoteAssembler extends ResourceAssemblerSupport<VoteData, VotePayload> {

    @Autowired
    private EntityLinks entityLinks;

    public VoteAssembler() {
        super(VoteResource.class, VotePayload.class);
    }

    @Override
    public VotePayload toResource(VoteData entity) {
        VotePayload payload = instantiateResource(entity);

        payload.add(ControllerLinkBuilder
                        .linkTo(ControllerLinkBuilder.methodOn(VoteResource.class).getVote(payload.getVoteId()))
                        .withSelfRel());

        // Link allVotesLink = entityLinks.linkToCollectionResource(VotePayload.class).withRel("votes");
        // payload.getLinks().add(allVotesLink);

        return payload;
    }

    @Override
    protected VotePayload instantiateResource(VoteData entity) {
        return VotePayload.Builder.builder().withId(entity.getId()).withEmail(entity.getEmail()).build();
    }
}
