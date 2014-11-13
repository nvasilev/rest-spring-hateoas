/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.poll.repository;

import java.util.Objects;

/**
 * A domain object representing a poll. Currently it is just a dummy POJO without any behaviour.
 *
 * @author Nikolay Vasilev
 */
public class Poll {

    /**
     * Identifier.
     */
    private final Long id;

    /**
     * Topic of the poll.
     */
    private final String topic;

    public Poll(Long id, String topic) {
        this.id = id;
        this.topic = topic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, topic);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Poll other = (Poll) obj;
        return Objects.equals(this.id, other.id) && Objects.equals(this.topic, other.topic);
    }

    public Long getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }
}
