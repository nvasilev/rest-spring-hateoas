/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.vote.repository;

import java.util.Objects;

/**
 * A domain object representing a vote. Currently it is just a dummy POJO without any behaviour.
 *
 * @author Nikolay Vasilev
 */
public class Vote {

    /**
     * Identifier.
     */
    private final Long id;

    /**
     * Voter's email.
     */
    private final String email;

    public Vote(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Vote other = (Vote) obj;
        return Objects.equals(this.id, other.id) && Objects.equals(this.email, other.email);
    }

}
