package com.yafuquen.pagersocket.data.service.socket;

import com.yafuquen.pagersocket.data.service.TeamUpdateService;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class TeamUpdateServiceImpl implements TeamUpdateService {

    private final String url;

    private final OkHttpClient okHttpClient;

    private final List<EventListener> eventListeners = new ArrayList<>();

    private WebSocket webSocket;

    public TeamUpdateServiceImpl(String url, OkHttpClient okHttpClient) {
        this.url = url;
        this.okHttpClient = okHttpClient;
    }

    @Override
    public Observable<String> receiveEvents() {
        connect();
        return Observable.create(emitter -> {
            EventListener eventListener = new EventListener() {
                @Override
                public void onNewEvent(String event) {
                    emitter.onNext(event);
                }

                @Override
                public void onError(Throwable throwable) {
                    emitter.tryOnError(throwable);
                }
            };
            addListener(eventListener);
            setDisposable(emitter, eventListener);
        });
    }

    @Override
    public Observable<Void> updateState(String stateMessage) {
        connect();
        return Observable.create(emitter -> {
            EventListener eventListener = new EventListener() {
                @Override
                public void onNewEvent(String event) {
                    if (event != null && event.equals(stateMessage)) {
                        emitter.onComplete();
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    emitter.tryOnError(throwable);
                }
            };
            addListener(eventListener);
            if (webSocket.send(stateMessage)) {
                emitter.onComplete();
            } else {
                emitter.tryOnError(new ConnectException());
            }
            setDisposable(emitter, eventListener);
        });
    }

    private void setDisposable(ObservableEmitter emitter, EventListener eventListener) {
        emitter.setDisposable(new Disposable() {

            private boolean disposed;

            @Override
            public void dispose() {
                removeListener(eventListener);
                disposed = true;
            }

            @Override
            public boolean isDisposed() {
                return disposed;
            }
        });
    }

    private void connect() {
        if (webSocket == null)
            webSocket =
                    okHttpClient.newWebSocket(new Request.Builder().url(url).build(), new WebSocketListener() {

                        @Override
                        public void onMessage(final WebSocket webSocket, final String payload) {
                            if (payload != null) {
                                notifyNewEvent(payload);
                            }
                        }

                        @Override
                        public void onFailure(WebSocket webSocket, Throwable throwable, Response response) {
                            notifyError(throwable);
                        }
                    });
    }

    private void notifyError(Throwable throwable) {
        for (EventListener eventListener : getListeners()) {
            eventListener.onError(throwable);
        }
    }

    private void notifyNewEvent(String payload) {
        for (EventListener eventListener : getListeners()) {
            eventListener.onNewEvent(payload);
        }
    }

    private List<EventListener> getListeners() {
        return new ArrayList<>(eventListeners);
    }

    private void addListener(EventListener listener) {
        eventListeners.add(listener);
    }

    private void removeListener(EventListener eventListener) {
        eventListeners.remove(eventListener);
        if (eventListeners.isEmpty()) {
            webSocket.close(1000, "Closed");
            webSocket = null;
        }
    }

    interface EventListener {

        void onNewEvent(String event);

        void onError(Throwable throwable);
    }
}
