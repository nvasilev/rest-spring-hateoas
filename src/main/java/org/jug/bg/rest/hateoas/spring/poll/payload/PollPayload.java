/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.poll.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jug.bg.rest.hateoas.spring.alternative.payload.AlternativePayload;
import org.jug.bg.rest.hateoas.spring.poll.repository.PollData;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.Relation;

@Relation(value = "poll", collectionRelation = "polls")
@JsonPropertyOrder({"id", "topic"})
public class PollPayload extends Resources<AlternativePayload> {

    private final PollData pollData;

    public PollPayload(Builder builder) {
        super(builder.alternatives, builder.links);
        this.pollData = builder.pollData;
    }

    @JsonProperty("id")
    public Long getPollId() {
        return pollData.getId();
    }

    @JsonProperty("topic")
    public String getTopic() {
        return pollData.getTopic();
    }

    public static class Builder {

        public PollData pollData;

        private Iterable<AlternativePayload> alternatives;

        private Iterable<Link> links;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withPollData(PollData pollData) {
            this.pollData = pollData;
            return this;
        }

        public Builder withAlternatives(Iterable<AlternativePayload> alternatives) {
            this.alternatives = alternatives;
            return this;
        }

        public Builder withLinks(Iterable<Link> links) {
            this.links = links;
            return this;
        }

        public PollPayload build() {
            return new PollPayload(this);
        }
    }
}
