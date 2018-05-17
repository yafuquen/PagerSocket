package com.yafuquen.pagersocket.data.service;

import io.reactivex.Observable;

public interface TeamUpdateService {

    Observable<String> receiveEvents();

    Observable<Void> updateState(String stateMessage);
}
