package com.yafuquen.pagersocket.domain.model.event;

public class StateEvent extends Event {

    public static final String EVENT_NAME = "state_change";

    private final String user;

    private final String state;

    public StateEvent(String event, String user, String state) {
        super(event);
        this.user = user;
        this.state = state;
    }

    public StateEvent(String user, String state) {
        super(EVENT_NAME);
        this.user = user;
        this.state = state;
    }

    public String getUser() {
        return user;
    }

    public String getState() {
        return state;
    }
}
