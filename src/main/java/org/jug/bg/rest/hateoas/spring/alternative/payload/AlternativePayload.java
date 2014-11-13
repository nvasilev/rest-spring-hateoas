/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.alternative.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

/**
 * Payload which is being serialized as a body when an alternative resource is requested.
 *
 * @author Nikolay Vasilev
 */
// @Relation defines the key in the "_embedded" section in which an alternative appears. When the alternative is more
// than one is picked the plural form.
@Relation(value = "alternative", collectionRelation = "alternatives")
@JsonPropertyOrder({"alternativeId", "value"})
@JsonDeserialize(builder = AlternativePayload.Builder.class) // used when writing rest clients
public class AlternativePayload extends ResourceSupport {

    @JsonProperty("id")
    private Long alternativeId;

    @JsonProperty("value")
    private String value;

    @JsonProperty("voteCount")
    private Integer votesCount;

    private AlternativePayload(Builder builder) {
        this.alternativeId = builder.alternativeId;
        this.value = builder.value;
        this.votesCount = builder.votesCount;
    }

    public Long getAlternativeId() {
        return alternativeId;
    }

    public String getValue() {
        return value;
    }

    public Integer getVotesCount() {
        return votesCount;
    }

    public static class Builder {

        private Long alternativeId;

        private String value;

        private Integer votesCount;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        @JsonSetter("id")
        public Builder withId(Long alternativeId) {
            this.alternativeId = alternativeId;
            return this;
        }

        @JsonSetter("value")
        public Builder withValue(String value) {
            this.value = value;
            return this;
        }

        @JsonSetter("votesCount")
        public Builder withVotesCount(Integer votesCount) {
            this.votesCount = votesCount;
            return this;
        }

        public AlternativePayload build() {
            return new AlternativePayload(this);
        }
    }
}
