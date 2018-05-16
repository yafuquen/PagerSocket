package com.yafuquen.pagersocket.data;

import com.yafuquen.pagersocket.data.cache.RolesCache;
import com.yafuquen.pagersocket.data.mapper.EventMapper;
import com.yafuquen.pagersocket.data.mapper.TeamMateMapper;
import com.yafuquen.pagersocket.data.repository.TeamRepositoryImpl;
import com.yafuquen.pagersocket.data.service.RolesService;
import com.yafuquen.pagersocket.data.service.TeamService;
import com.yafuquen.pagersocket.data.service.TeamUpdateService;
import com.yafuquen.pagersocket.domain.model.TeamMate;
import com.yafuquen.pagersocket.domain.model.event.Event;
import com.yafuquen.pagersocket.domain.repository.TeamRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TeamRepositoryTests {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private TeamService teamService;

    @Mock
    private RolesService rolesService;

    @Mock
    private TeamUpdateService teamUpdateService;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private TeamMateMapper teamMateMapper;

    @Mock
    private RolesCache rolesCache;

    private TeamRepository teamRepository;

    @Before
    public void setup() {
        teamRepository = new TeamRepositoryImpl(teamService, rolesService, teamUpdateService, teamMateMapper, eventMapper, rolesCache);
    }

    @Test
    public void getSuccess() {
        when(teamService.team()).thenReturn(Observable.just(Collections.emptyList()));
        when(rolesService.roles()).thenReturn(Observable.just(Collections.emptyMap()));
        when(rolesCache.getRoles()).thenReturn(Observable.empty());
        TestObserver<List<TeamMate>> testObserver = new TestObserver<>();
        teamRepository.get().subscribe(testObserver);
        testObserver.assertComplete();
        verify(teamMateMapper).transform(Collections.emptyList(), Collections.emptyMap());
    }

    @Test
    public void receiveUpdates() {
        String payload = "payload";
        Event event = new Event("event");
        when(teamUpdateService.receiveEvents()).thenReturn(Observable.just(payload));
        when(eventMapper.transform(payload)).thenReturn(event);
        TestObserver<Event> testObserver = new TestObserver<>();
        teamRepository.receiveUpdates().subscribe(testObserver);
        verify(eventMapper).transform(payload);
        testObserver.assertResult(event);
    }

    @Test
    public void updateStatus() {
        String username = "username";
        String newStatus = "status";
        when(teamUpdateService.updateStatus(anyString())).thenReturn(Observable.empty());
        when(eventMapper.buildMessage(username, newStatus)).thenReturn("payload");
        TestObserver<Void> testObserver = new TestObserver<>();
        teamRepository.updateStatus(username, newStatus).subscribe(testObserver);
        verify(eventMapper).buildMessage(username, newStatus);
        testObserver.assertComplete();
    }
}
