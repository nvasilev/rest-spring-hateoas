package org.jug.bg.rest.hateoas.spring.poll.resource;

public class PollRequestBody {

    private String topic;

    public PollRequestBody() {
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
