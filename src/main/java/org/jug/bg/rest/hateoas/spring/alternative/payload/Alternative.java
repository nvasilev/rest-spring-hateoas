package org.jug.bg.rest.hateoas.spring.alternative.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value = "alternative", collectionRelation = "alternatives")
@JsonPropertyOrder({"alternativeId", "value"})
@JsonDeserialize(builder = Alternative.Builder.class)
public class Alternative extends ResourceSupport {

    private Long alternativeId;

    private String value;

    // TODO: list of votes

    private Alternative(Builder builder) {
        this.alternativeId = builder.alternativeId;
        this.value = builder.value;
    }

    public static class Builder {

        private Long alternativeId;

        private String value;

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

        public Alternative build() {
            return new Alternative(this);
        }
    }
}
