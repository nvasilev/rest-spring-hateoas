package org.jug.bg.rest.hateoas.spring.vote.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class VoteRepository {

    private long counter=1;

    private Map<Long, VoteData> store = new HashMap<>();

    public VoteData findVote(Long id) {
        return store.get(id);
    }

    // FIXME: CQS
    public VoteData createVote(String email) {
        VoteData newVote = new VoteData(counter++, email);
        store.put(newVote.getId(), newVote);
        return newVote;
    }


}
