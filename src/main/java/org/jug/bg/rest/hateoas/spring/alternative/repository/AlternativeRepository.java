package org.jug.bg.rest.hateoas.spring.alternative.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AlternativeRepository {

    private static long counter = 1;

    private Map<Long, AlternativeData> store = new HashMap<>();

    public AlternativeData find(Long alternativeId) {
        return store.get(alternativeId);
    }

    // FIXME: CQS
    public AlternativeData storeAlternative(String text) {
        AlternativeData alternative = new AlternativeData(counter++, text);
        store.put(alternative.getAlternativeId(), alternative);
        return alternative;
    }
}
