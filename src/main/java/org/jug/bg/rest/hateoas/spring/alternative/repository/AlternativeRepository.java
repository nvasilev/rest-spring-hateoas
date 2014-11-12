/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.alternative.repository;

import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Dummy in-memory implementation of an Alternative repository.
 *
 * <p><strong>Note:</strong> Built for sake of simplicity and keeping the focus on REST-related part of the project.</p>
 */
@Repository
public class AlternativeRepository {

    private static long counter = 1;

    /**
     * In-memory data store implementation.
     */
    private Map<Long, Map<Long, Alternative>> store = new HashMap<>();

    /**
     * Retrieves all alternatives for a poll id.
     *
     * @param alternativeParameter Alternative parameter used for alternatives retrieval.
     *
     * @return Returns all alternatives for a poll id if exist or an empty list otherwise.
     */
    public List<Alternative> retrieveAllAlternatives(AlternativeParameter alternativeParameter) {
        Long pollId = alternativeParameter.getPollId();
        if (!store.containsKey(pollId)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(store.get(pollId).values());
    }

    /**
     * Retrieves an alternative by poll and alternative ids.
     *
     * @param alternativeParameter Alternative parameter used for alternative retrieval.
     *
     * @return Returns an alternative identified by poll and alternative ids if exists or <code>null<code/> otherwise.
     */
    public Alternative retrieveAlternative(AlternativeParameter alternativeParameter) {
        Alternative alternative = null;
        try {
            Long pollId = alternativeParameter.getPollId();
            Long alternativeId = alternativeParameter.getAlternativeId();

            alternative = store.get(pollId).get(alternativeId);
        } catch (NullPointerException npe) {
            // I know, NPE handling is lame as hell but not the repository is the focus of this project, so live with it :p
        }
        return alternative;
    }


    /**
     * Stores new alternative data for given poll.
     *
     * @param alternativeParameter Alternative parameter used during the storing.
     *
     * @return Returns the newly stored alternative.
     */
    public Alternative storeAlternative(AlternativeParameter alternativeParameter) {

        Long pollId = alternativeParameter.getPollId();
        if(!store.containsKey(pollId)) {
            store.put(pollId, new HashMap<>());
        }

        // FIXME: delegate Alternative's creation to a builder or a factory?
        Alternative alternative = new Alternative(counter++, alternativeParameter.getValue());

        Map<Long, Alternative> alternativeMap = store.get(pollId);
        alternativeMap.put(alternative.getAlternativeId(), alternative);

        return alternative;
    }

}
