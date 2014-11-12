/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.alternative.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Class used for to transfer alternative-related data across the various layers of the application in an encapsulated
 * manner.
 *
 * <p>See "Parameter object" refactoring design pattern.</p>
 *
 * <p><strong>Note:</strong> The reason this class is decorated with JSON-related annotations is because it is being
 * used for deserializing the payload which comes from the consumer of our REST service when creating a Vote.</p>
 *
 * @author Nikolay Vasilev
 */
@JsonPropertyOrder({"pollId", "alternativeId", "value"})
@JsonDeserialize(builder = AlternativeParameter.Builder.class)
public class AlternativeParameter {

    private final Long pollId;

    private final Long alternativeId;

    private final String value;

    public AlternativeParameter(Builder builder) {
        this.pollId = builder.pollId;
        this.alternativeId = builder.alternativeId;
        this.value = builder.value;
    }

    public Long getPollId() {
        return pollId;
    }

    public Long getAlternativeId() {
        return alternativeId;
    }

    public String getValue() {
        return value;
    }

    public static class Builder {

        private Long pollId;

        private Long alternativeId;

        private String value;

        private Builder() {}

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

        @JsonProperty("value")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public Builder withValue(String value) {
            this.value = value;
            return this;
        }

        public AlternativeParameter build() {
            return new AlternativeParameter(this);
        }
    }
}
