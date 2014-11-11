/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.poll.repository;

import java.util.Objects;

public class PollData {

    private Long id;

    private String topic;

    public PollData(Long id, String topic) {
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
        final PollData other = (PollData) obj;
        return Objects.equals(this.id, other.id) && Objects.equals(this.topic, other.topic);
    }

    public Long getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }
}
