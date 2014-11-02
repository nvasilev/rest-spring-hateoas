package org.jug.bg.rest.hateoas.spring.alternative.repository;

import java.util.Objects;

public class AlternativeData {

    private Long alternativeId;

    private String value;

    public AlternativeData(Long alternativeId, String value) {
        this.alternativeId = alternativeId;
        this.value = value;
    }

    public Long getAlternativeId() {
        return alternativeId;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alternativeId, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AlternativeData other = (AlternativeData) obj;
        return Objects.equals(this.alternativeId, other.alternativeId) && Objects.equals(this.value, other.value);
    }
}
