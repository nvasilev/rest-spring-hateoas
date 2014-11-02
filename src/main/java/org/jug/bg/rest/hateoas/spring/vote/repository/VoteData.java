package org.jug.bg.rest.hateoas.spring.vote.repository;

import java.util.Objects;

public class VoteData {

    private Long id;

    private String email;

    public VoteData(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        final VoteData other = (VoteData) obj;
        return Objects.equals(this.id, other.id) && Objects.equals(this.email, other.email);
    }
}
