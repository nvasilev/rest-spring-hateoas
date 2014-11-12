/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.alternative.repository;

import java.util.Objects;

/**
 * A domain object representing an alternative. Currently it is just a dummy POJO without any behaviour.
 *
 * @author Nikolay Vasilev
 */
public class Alternative {

    private Long alternativeId;

    private String value;

    public Alternative(Long alternativeId, String value) {
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
        final Alternative other = (Alternative) obj;
        return Objects.equals(this.alternativeId, other.alternativeId) && Objects.equals(this.value, other.value);
    }
}
