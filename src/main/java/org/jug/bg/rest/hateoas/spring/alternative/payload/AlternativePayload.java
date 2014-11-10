package org.jug.bg.rest.hateoas.spring.alternative.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value = "alternative", collectionRelation = "alternatives")
@JsonPropertyOrder({"alternativeId", "value"})
@JsonDeserialize(builder = AlternativePayload.Builder.class)
public class AlternativePayload extends ResourceSupport {

    private Long alternativeId;

    private String value;

    private Integer votesCount;

    // TODO: list of votes

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

        @JsonProperty("alternativeId")
        public Builder withId(Long alternativeId) {
            this.alternativeId = alternativeId;
            return this;
        }

        @JsonProperty("value")
        public Builder withValue(String value) {
            this.value = value;
            return this;
        }

        public Builder withVotesCount(Integer votesCount) {
            this.votesCount = votesCount;
            return this;
        }

        public AlternativePayload build() {
            return new AlternativePayload(this);
        }
    }
}
