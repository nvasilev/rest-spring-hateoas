package org.jug.bg.rest.hateoas.spring.vote.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value = "vote", collectionRelation = "votes")
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
