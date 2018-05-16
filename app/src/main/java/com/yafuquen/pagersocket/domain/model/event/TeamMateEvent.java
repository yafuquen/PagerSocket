package com.yafuquen.pagersocket.domain.model.event;

import com.yafuquen.pagersocket.domain.model.TeamMate;

public class TeamMateEvent extends Event {

    public static final String EVENT_NAME = "user_new";

    private final TeamMate teamMate;

    public TeamMateEvent(String event, TeamMate teamMate) {
        super(event);
        this.teamMate = teamMate;
    }

    public TeamMate getTeamMate() {
        return teamMate;
    }
}
