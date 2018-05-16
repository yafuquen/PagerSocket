package com.yafuquen.pagersocket.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yafuquen.pagersocket.data.cache.RolesCache;
import com.yafuquen.pagersocket.data.mapper.EventMapper;
import com.yafuquen.pagersocket.domain.model.event.Event;
import com.yafuquen.pagersocket.domain.model.event.StateEvent;
import com.yafuquen.pagersocket.domain.model.event.TeamMateEvent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;

import io.reactivex.Observable;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;

public class EventMapperTests {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private RolesCache rolesCache;

    private EventMapper eventMapper;

    @Before
    public void setup() {
        eventMapper = new EventMapper(new Gson(), rolesCache);
    }

    @Test
    public void userEvent() {
        when(rolesCache.getRoles()).thenReturn(Observable.just(Collections.emptyMap()));
        JsonObject payload = generateUserPayload();
        Event teamEvent = eventMapper.transform(payload.toString());
        assertTrue(teamEvent instanceof TeamMateEvent);
        TeamMateEvent teamMateEvent = (TeamMateEvent) teamEvent;
        assertEquals(payload.getAsJsonObject("user").get("name").getAsString(), teamMateEvent.getTeamMate().getName());
        assertEquals(payload.getAsJsonObject("user").get("avatar").getAsString(), teamMateEvent.getTeamMate().getAvatar());
    }

    @Test
    public void stateEvent() {
        JsonObject payload = generateStatePayload();
        Event teamEvent = eventMapper.transform(payload.toString());
        assertTrue(teamEvent instanceof StateEvent);
        StateEvent stateEvent = (StateEvent) teamEvent;
        assertEquals(payload.get("user").getAsString(), stateEvent.getUser());
        assertEquals(payload.get("state").getAsString(), stateEvent.getState());
    }

    private JsonObject generateUserPayload() {
        JsonObject payload = new JsonObject();
        payload.addProperty("event", "user_new");
        JsonObject user = new JsonObject();
        user.addProperty("name", "name");
        user.addProperty("avatar", "avatar");
        user.addProperty("github", "github");
        user.addProperty("role", 1);
        JsonArray languages = new JsonArray();
        languages.add("language1");
        user.add("languages", languages);
        JsonArray tags = new JsonArray();
        user.add("tags", tags);
        user.addProperty("location", "us");
        payload.add("user", user);
        return payload;
    }

    private JsonObject generateStatePayload() {
        JsonObject payload = new JsonObject();
        payload.addProperty("event", "state_change");
        payload.addProperty("user", "user");
        payload.addProperty("state", "state");
        return payload;
    }
}
