/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.poll.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jug.bg.rest.hateoas.spring.alternative.payload.AlternativePayload;
import org.jug.bg.rest.hateoas.spring.poll.repository.Poll;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.Relation;

/**
 * Payload which is being serialized as a body when a poll resource is requested.
 * <p/>
 * <p>Mind that when а domain object and the payload are very similar in terms of structure what could be done is the
 * payload to be implemented as a wrapper around the domain object. Thus there is no code duplication - the payload
 * acts as a proxy. In that way the domain class is used for domain-specific logic, while the payload for (REST)
 * representation.</p>
 * <p/>
 * <p><strong>Note:</strong> Extending {@link Resources} link and embedded resources handling is get for free.</p>
 *
 * @author Nikolay Vasilev
 */
@Relation(value = "poll", collectionRelation = "polls")
@JsonPropertyOrder({"id", "topic"})
public class PollPayload extends Resources<AlternativePayload> {

    private final Poll pollData;

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

        public Poll pollData;

        private Iterable<AlternativePayload> alternatives;

        private Iterable<Link> links;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withPollData(Poll pollData) {
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
