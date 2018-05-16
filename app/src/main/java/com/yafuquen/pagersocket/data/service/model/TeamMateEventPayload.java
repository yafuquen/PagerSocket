package com.yafuquen.pagersocket.data.service.model;

import com.yafuquen.pagersocket.domain.model.event.Event;

public class TeamMateEventPayload extends Event {

    private final TeamMateResponse user;

    public TeamMateEventPayload(String event, TeamMateResponse user) {
        super(event);
        this.user = user;
    }

    public TeamMateResponse getUser() {
        return user;
    }
}
