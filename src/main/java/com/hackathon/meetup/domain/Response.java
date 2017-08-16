package com.hackathon.meetup.domain;

/**
 * Created by David Turk on 8/10/17.
 */
public class Response<T> {
    private T payload;

    public Response() {
    }

    public Response(T payload) {
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Response{" +
                "payload=" + payload.toString() +
                '}';
    }
}
