package com.yafuquen.pagersocket.domain.interactor;

import com.yafuquen.pagersocket.data.publisher.EventPublisher;
import com.yafuquen.pagersocket.domain.model.TeamMate;
import com.yafuquen.pagersocket.domain.model.event.Event;
import com.yafuquen.pagersocket.domain.model.event.NoConnectionEvent;
import com.yafuquen.pagersocket.domain.model.event.StateEvent;
import com.yafuquen.pagersocket.domain.repository.TeamRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;

public class TeamInteractor extends UseCase<List<TeamMate>> {


    private final TeamRepository teamRepository;

    private final EventPublisher eventPublisher;

    @Inject
    public TeamInteractor(Scheduler scheduler, Scheduler observerScheduler, TeamRepository teamRepository,
                          EventPublisher eventPublisher) {
        super(scheduler, observerScheduler);
        this.teamRepository = teamRepository;
        this.eventPublisher = eventPublisher;
    }

    public <O extends Observer<Void> & Disposable> void updateState(String username, String newState, O disposableObserver) {
        Observable<Void> sendObservable = execute(teamRepository.updateState(username, newState)
                .onErrorResumeNext(throwable -> {
                    return Observable.empty();
                })).doOnComplete(() -> eventPublisher.notifyEvent(new StateEvent(username, newState)));
        addDisposable(sendObservable, disposableObserver);
    }

    public void requestUpdates(Consumer<Event> onEvent) {
        requestUpdates();
        subscribeToPublisher(eventPublisher.getEvents(), onEvent);
    }

    public void requestUpdates(Consumer<Event> onEvent, Predicate<? super Event> predicate) {
        requestUpdates();
        subscribeToPublisher(eventPublisher.getEvents().filter(predicate), onEvent);
    }

    @Override
    protected Observable<List<TeamMate>> createObservable() {
        return teamRepository.get();
    }

    private void requestUpdates() {
        addDisposable(execute(teamRepository.receiveUpdates()), new DisposableObserver<Event>() {
            @Override
            public void onNext(Event event) {
                eventPublisher.notifyEvent(event);
            }

            @Override
            public void onError(Throwable e) {
                eventPublisher.notifyEvent(new NoConnectionEvent());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void subscribeToPublisher(Observable<Event> observable, Consumer<Event> onEvent) {
        addDisposable(observable.subscribe(onEvent));
    }
}
