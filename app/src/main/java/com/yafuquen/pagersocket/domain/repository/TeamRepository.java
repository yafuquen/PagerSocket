package com.yafuquen.pagersocket.domain.repository;

import com.yafuquen.pagersocket.domain.model.event.Event;
import com.yafuquen.pagersocket.domain.model.TeamMate;

import java.util.List;

import io.reactivex.Observable;

public interface TeamRepository {

    Observable<List<TeamMate>> get();

    Observable<Void> updateState(String username, String newState);

    Observable<Event> receiveUpdates();
}
