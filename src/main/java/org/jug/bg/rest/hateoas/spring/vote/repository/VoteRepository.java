package org.jug.bg.rest.hateoas.spring.vote.repository;

import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Dummy in-memory implementation of a Vote repository.
 * <br />
 * <strong>Note:</strong> Built for sake of simplicity and focusing on REST-related part of the project.
 */
@Repository
public class VoteRepository {

    private static long counter = 1;

    /**
     * In-memory data store implementation.
     */
    private Map<Long, Map<Long, Map<Long, Vote>>> store = new HashMap<>();

    /**
     * Retrieves a vote by poll, alternative and vote ids.
     *
     * @param voteParameter Vote parameter used for vote retrieval.
     *
     * @return Returns vote identified by poll, alternative and vote ids if exists or <code>null<code/> otherwise.
     */
    public Vote retrieveVote(VoteParameter voteParameter) {
        Vote vote = null;
        try {
            Long pollId = voteParameter.getPollId();
            Long alternativeId = voteParameter.getAlternativeId();
            Long voteId = voteParameter.getVoteId();

            vote = store.get(pollId).get(alternativeId).get(voteId);
        } catch (NullPointerException exception) {
            // lame as hell but not the repository is the focus of this project.
        }
        return vote;
    }

    /**
     * Retrieves all votes by poll and alternative ids.
     *
     * @param voteParameter Vote parameter used for votes retrieval.
     *
     * @return Returns all votes by poll and alternative ids if exist or empty array otherwise.
     */
    public List<Vote> retrieveAllVotesForAlternativeAndPoll(VoteParameter voteParameter) {
        List<Vote> votes;
        try {
            Long pollId = voteParameter.getPollId();
            Long alternativeId = voteParameter.getAlternativeId();

            votes = new ArrayList<>(store.get(pollId).get(alternativeId).values());
        } catch (NullPointerException npe) {
            // lame as hell but not the repository is the focus of this project.
            votes = Collections.emptyList();
        }
        return votes;
    }

    /**
     * Stores new vote data for given poll and alternative.
     *
     * @param voteParameter Vote parameter used during the storing.
     *
     * @return Returns the newly stored vote.
     */
    public Vote createVote(VoteParameter voteParameter) {

        Long pollId = voteParameter.getPollId();
        if (!store.containsKey(pollId)) {
            store.put(pollId, new HashMap<>());
        }


        Long alternativeId = voteParameter.getAlternativeId();
        Map<Long, Map<Long, Vote>> alternativesMap = store.get(pollId);
        if (!alternativesMap.containsKey(alternativeId)) {
            alternativesMap.put(alternativeId, new HashMap<>());
        }

        // FIXME: delegate Vote's creation to a builder or a factory
        Vote vote = new Vote(counter++, voteParameter.getEmail());

        // storing the vote
        Map<Long, Vote> voteMap = alternativesMap.get(alternativeId);
        voteMap.put(vote.getId(), vote);

        return vote;
    }
}
