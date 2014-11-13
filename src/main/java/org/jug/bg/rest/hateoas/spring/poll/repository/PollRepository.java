/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.poll.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Dummy in-memory implementation of a Poll repository.
 *
 * <p><strong>Note:</strong> Built for sake of simplicity and keeping the focus on REST-related part of the project.</p>
 *
 * @author Nikolay Vasilev
 */
@Repository
public class PollRepository {

    private long counter = 1;

    /**
     * In-memory data store implementation.
     */
    private Map<Long, Poll> store = new HashMap<>();

    /**
     * Retrieves a poll by id.
     *
     * @param pollParameter Poll parameter used for vote retrieval.
     *
     * @return Returns poll identified by poll id if exists or <code>null<code/> otherwise.
     */
    public Poll retrievePoll(PollParameter pollParameter) {
        Long pollId = pollParameter.getPollId();
        return store.get(pollId);
    }

    /**
     * Stores new poll.
     *
     * @param topic Poll parameter used during the storing.
     *
     * @return Returns the newly stored poll.
     */
    public Poll storePoll(PollParameter topic) {
        Poll data = new Poll(counter++, topic.getTopic());
        store.put(data.getId(), data);
        return data;
    }

    /**
     * Removes a poll by id.
     *
     * @param id Id of the poll which is to be removed.
     */
    public void removePoll(Long id) {
        store.remove(id);
    }
}
