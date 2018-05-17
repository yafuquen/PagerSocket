package com.yafuquen.pagersocket.data.mapper;

import com.google.gson.Gson;
import com.yafuquen.pagersocket.data.cache.RolesCache;
import com.yafuquen.pagersocket.data.service.model.TeamMateEventPayload;
import com.yafuquen.pagersocket.data.service.model.TeamMateResponse;
import com.yafuquen.pagersocket.domain.model.TeamMate;
import com.yafuquen.pagersocket.domain.model.event.Event;
import com.yafuquen.pagersocket.domain.model.event.StateEvent;
import com.yafuquen.pagersocket.domain.model.event.TeamMateEvent;

import javax.inject.Inject;

public class EventMapper {

    private final Gson gson;

    private final RolesCache rolesCache;

    @Inject
    public EventMapper(Gson gson, RolesCache rolesCache) {
        this.gson = gson;
        this.rolesCache = rolesCache;
    }

    public Event transform(String payload) {
        try {
            Event event = gson.fromJson(payload, Event.class);
            switch (event.getEvent()) {
                case TeamMateEvent.EVENT_NAME:
                    TeamMateEventPayload teamMateEventPayload = gson.fromJson(payload, TeamMateEventPayload.class);
                    return transform(teamMateEventPayload);
                case StateEvent.EVENT_NAME:
                    return gson.fromJson(payload, StateEvent.class);
            }
            return event;
        } catch (Exception ex) {
            return new Event("");
        }
    }

    private TeamMateEvent transform(TeamMateEventPayload teamMateEventPayload) {
        TeamMateResponse teamMateResponse = teamMateEventPayload.getUser();
        return new TeamMateEvent(teamMateEventPayload.getEvent(), new TeamMate(teamMateResponse.getName(),
                teamMateResponse.getAvatar(), teamMateResponse.getGithub(), rolesCache.getRole(Integer.toString(teamMateResponse.getRole())),
                teamMateResponse.getGender(), teamMateResponse.getLanguages(), teamMateResponse.getTags(), teamMateResponse.getLocation()));
    }

    public String buildMessage(String username, String state) {
        return gson.toJson(new StateEvent(StateEvent.EVENT_NAME, username, state));
    }
}
