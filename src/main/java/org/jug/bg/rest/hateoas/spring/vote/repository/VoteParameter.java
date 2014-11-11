/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.vote.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Class used for to transfer vote-related data across various layers in an encapsulated manner.
 * <br />
 * See "Parameter object" refactoring design pattern.
 *
 * @author Nikolay Vasilev
 */
@JsonPropertyOrder({"pollId", "alternativeId", "voteId", "email"})
@JsonDeserialize(builder = VoteParameter.Builder.class)
public class VoteParameter {

    private final Long pollId;

    private final Long alternativeId;

    private final Long voteId;

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

    public static class Builder implements org.jug.bg.rest.hateoas.spring.common.Builder<VoteParameter> {

        private Long pollId;

        private Long alternativeId;

        private Long voteId;

        private String email;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        @JsonProperty("pollId")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public Builder withPollId(Long pollId) {
            this.pollId = pollId;
            return this;
        }

        @JsonProperty("alternativeId")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public Builder withAlternativeId(Long alternativeId) {
            this.alternativeId = alternativeId;
            return this;
        }

        @JsonProperty("voteId")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public Builder withVoteId(Long voteId) {
            this.voteId = voteId;
            return this;
        }

        @JsonProperty("email")
        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public VoteParameter build() {
            return new VoteParameter(this);
        }
    }
}
