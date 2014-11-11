/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.poll.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PollRepository {

    private long counter = 1;

    private Map<Long, PollData> store = new HashMap<>();

    public PollData findPoll(Long id) {
        return store.get(id);
    }

    public PollData storePoll(String topic) {
        PollData data = new PollData(counter++, topic);
        store.put(data.getId(), data);
        return data;
    }

    public void removePoll(Long id) {
        store.remove(id);
    }
}
