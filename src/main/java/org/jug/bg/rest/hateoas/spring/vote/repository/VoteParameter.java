/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.vote.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Class used for to transfer vote-related data across the various layers of the application in an encapsulated manner.
 *
 * <p>See "Parameter object" refactoring design pattern.</p>
 *
 * <p><strong>Note:</strong> The reason this class is decorated with JSON-related annotations is because it is being
 * used for deserializing the payload which comes from the consumer of our REST service when creating a Vote.</p>
 *
 * @author Nikolay Vasilev
 */
@JsonPropertyOrder({"pollId", "alternativeId", "voteId", "email"})
@JsonDeserialize(builder = VoteParameter.Builder.class)
public class VoteParameter {

    @JsonProperty("pollId")
    private final Long pollId;

    @JsonProperty("alternativeId")
    private final Long alternativeId;

    @JsonProperty("voteId")
    private final Long voteId;

    @JsonProperty("email")
    private final String email;

    private VoteParameter(Builder builder) {
        this.pollId = builder.pollId;
        this.alternativeId = builder.alternativeId;
        this.voteId = builder.voteId;
        this.email = builder.email;
    }

    public Long getPollId() {
        return pollId;
    }

    public Long getAlternativeId() {
        return alternativeId;
    }

    public Long getVoteId() {
        return voteId;
    }

    public String getEmail() {
        return email;
    }

    public static class Builder {

        private Long pollId;

        private Long alternativeId;

        private Long voteId;

        private String email;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        @JsonSetter("pollId")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public Builder withPollId(Long pollId) {
            this.pollId = pollId;
            return this;
        }

        @JsonSetter("alternativeId")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public Builder withAlternativeId(Long alternativeId) {
            this.alternativeId = alternativeId;
            return this;
        }

        @JsonSetter("voteId")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public Builder withVoteId(Long voteId) {
            this.voteId = voteId;
            return this;
        }

        @JsonSetter("email")
        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public VoteParameter build() {
            return new VoteParameter(this);
        }
    }
}
