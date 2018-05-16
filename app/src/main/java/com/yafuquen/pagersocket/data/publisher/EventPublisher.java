package com.yafuquen.pagersocket.data.publisher;

import com.yafuquen.pagersocket.domain.model.event.Event;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class EventPublisher {

    private final PublishSubject<Event> subject = PublishSubject.create();

    public void notifyEvent(Event event) {
        subject.onNext(event);
    }

    public Observable<Event> getEvents() {
        return subject;
    }
}
