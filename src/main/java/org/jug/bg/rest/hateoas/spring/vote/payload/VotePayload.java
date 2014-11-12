/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.vote.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

/**
 * Payload which is being serialized as a body when a vote resource is requested.
 *
 * @author Nikolay Vasilev
 */
@Relation(value = "vote", collectionRelation = "votes") // defines the key in the "_embedded" section in which a vote appears
@JsonPropertyOrder({"voteId", "email"})
@JsonDeserialize(builder = VotePayload.Builder.class)
public class VotePayload extends ResourceSupport {

    private Long voteId;

    private String email;

    private VotePayload(Builder builder) {
        this.voteId = builder.voteId;
        this.email = builder.email;
    }

    public Long getVoteId() {
        return voteId;
    }

    public String getEmail() {
        return email;
    }

    public static class Builder {

        private Long voteId;

        private String email;

        private Builder(){}

        public static Builder builder() {
            return new Builder();
        }

        @JsonProperty("id")
        public Builder withId(Long voteId) {
            this.voteId = voteId;
            return this;
        }

        @JsonProperty("email")
        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public VotePayload build() {
            return new VotePayload(this);
        }
    }
}
