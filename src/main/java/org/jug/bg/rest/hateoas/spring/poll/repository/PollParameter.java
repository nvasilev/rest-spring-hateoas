/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.poll.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Class used for to transfer poll-related data across the various layers of the application in an encapsulated manner.
 * <p/>
 * <p>See "Parameter object" refactoring design pattern.</p>
 * <p/>
 * <p><strong>Note:</strong> The reason this class is decorated with JSON-related annotations is because it is being
 * used for deserializing the payload which comes from the consumer of our REST service when creating a Vote.</p>
 *
 * @author Nikolay Vasilev
 */
@JsonPropertyOrder({"pollId", "alternativeId", "value"})
@JsonDeserialize(builder = PollParameter.Builder.class)
public class PollParameter {

    @JsonProperty("pollId")
    private final Long pollId;

    @JsonProperty("topic")
    private final String topic;

    private PollParameter(Builder builder) {
        this.pollId = builder.pollId;
        this.topic = builder.topic;
    }

    public Long getPollId() {
        return pollId;
    }

    public String getTopic() {
        return topic;
    }

    public static class Builder {

        private Long pollId;

        private String topic;

        private Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        @JsonSetter("pollId")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public Builder withPollId(Long pollId){
            this.pollId = pollId;
            return this;
        }

        @JsonSetter("topic")
        public Builder withTopic(String topic) {
            this.topic = topic;
            return this;
        }

        public PollParameter build() {
            return new PollParameter(this);
        }
    }
}
