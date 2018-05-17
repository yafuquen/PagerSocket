package com.yafuquen.pagersocket.domain.interactor;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

abstract class UseCase<T> {

    private final Scheduler scheduler;

    private final Scheduler observerScheduler;

    private final CompositeDisposable disposables = new CompositeDisposable();

    UseCase(Scheduler scheduler, Scheduler observerScheduler) {
        this.scheduler = scheduler;
        this.observerScheduler = observerScheduler;
    }

    public <O extends Observer<? super T> & Disposable> void execute(O disposableObserver) {
        Observable<T> observable = execute(createObservable());
        addDisposable(observable, disposableObserver);
    }

    public void dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    protected abstract Observable<T> createObservable();

    <E> Observable<E> execute(Observable<E> observable) {
        return observable.subscribeOn(scheduler).observeOn(observerScheduler);
    }

    <E, O extends Observer<? super E> & Disposable> void addDisposable(Observable<E> observable, O disposableObserver) {
        addDisposable(observable.subscribeWith(disposableObserver));
    }

    void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }
}
