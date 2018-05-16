package com.yafuquen.pagersocket.domain.di;

import com.yafuquen.pagersocket.data.publisher.EventPublisher;
import com.yafuquen.pagersocket.domain.interactor.TeamInteractor;
import com.yafuquen.pagersocket.domain.repository.TeamRepository;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

@Module
public class DomainModule {

    public static final String SUBSCRIBER_SCHEDULER = "subscribeOn";

    public static final String OBSERVER_SCHEDULER = "observeOn";

    @Provides
    TeamInteractor provideTeamInteractor(@Named(SUBSCRIBER_SCHEDULER) Scheduler scheduler,
                                         @Named(OBSERVER_SCHEDULER) Scheduler observerScheduler,
                                         TeamRepository teamRepository, EventPublisher eventPublisher) {
        return new TeamInteractor(scheduler, observerScheduler, teamRepository, eventPublisher);
    }
}
