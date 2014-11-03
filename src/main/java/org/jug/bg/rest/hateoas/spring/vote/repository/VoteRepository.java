package org.jug.bg.rest.hateoas.spring.vote.repository;

import org.jug.bg.rest.hateoas.spring.vote.payload.VotePayload;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class VoteRepository {

    private long counter=1;

    private Map<Long, VoteData> store = new HashMap<>();

    public VoteData findVote(Long id) {
        return store.get(id);
    }

    public List<VoteData> getAll(Long alternativeId) {
        return new ArrayList<>(store.values());
    }

    // FIXME: CQS
    public VoteData createVote(String email) {
        VoteData newVote = new VoteData(counter++, email);
        store.put(newVote.getId(), newVote);
        return newVote;
    }

}
