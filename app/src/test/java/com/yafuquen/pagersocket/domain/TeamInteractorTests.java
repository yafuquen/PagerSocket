package com.yafuquen.pagersocket.domain;

import com.yafuquen.pagersocket.data.publisher.EventPublisher;
import com.yafuquen.pagersocket.domain.interactor.TeamInteractor;
import com.yafuquen.pagersocket.domain.model.TeamMate;
import com.yafuquen.pagersocket.domain.model.event.Event;
import com.yafuquen.pagersocket.domain.model.event.StateEvent;
import com.yafuquen.pagersocket.domain.repository.TeamRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TeamInteractorTests {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private Consumer<Event> consumer;

    private TeamInteractor teamInteractor;

    @Before
    public void setup() {
        teamInteractor = new TeamInteractor(Schedulers.computation(), Schedulers.computation(), teamRepository, eventPublisher);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void execute() {
        List<TeamMate> team = createList();
        TestObserver<List<TeamMate>> testObserver = new TestObserver<>();
        when(teamRepository.get()).thenReturn(Observable.just(team));
        teamInteractor.execute(testObserver);
        verify(teamRepository).get();
        testObserver.awaitDone(500, TimeUnit.MILLISECONDS);
        testObserver.assertComplete();
        testObserver.assertResult(team);
    }

    @Test
    public void updateState() {
        String username = "username";
        String state = "newState";
        when(teamRepository.updateState(username, state)).thenReturn(Observable.empty());
        TestObserver<Void> testObserver = new TestObserver<>();
        teamInteractor.updateState(username, state, testObserver);
        testObserver.awaitDone(500, TimeUnit.MILLISECONDS);
        testObserver.assertComplete();
    }

    @Test
    public void updateStateError() {
        String username = "username";
        String state = "newState";
        when(teamRepository.updateState(username, state)).thenReturn(Observable.error(new IllegalArgumentException()));
        TestObserver<Void> testObserver = new TestObserver<>();
        teamInteractor.updateState(username, state, testObserver);
        testObserver.awaitDone(500, TimeUnit.MILLISECONDS);
        testObserver.assertComplete();
    }

    @Test
    public void requestUpdates() throws Exception {
        String username = "username";
        String state = "newState";
        Event event = new StateEvent(username, state);
        Observable<Event> events = Observable.just(event);
        when(teamRepository.receiveUpdates()).thenReturn(events);
        when(eventPublisher.getEvents()).thenReturn(events);
        teamInteractor.requestUpdates(consumer);
        verify(teamRepository).receiveUpdates();
        verify(eventPublisher).getEvents();
        verify(eventPublisher, timeout(500)).notifyEvent(event);
        verify(consumer, timeout(500)).accept(event);
    }

    private List<TeamMate> createList() {
        List<TeamMate> team = new ArrayList<>();
        team.add(new TeamMate("Name", "avatar", "github", "role",
                "Male", new ArrayList<>(), new ArrayList<>(), "us"));
        return team;
    }
}
