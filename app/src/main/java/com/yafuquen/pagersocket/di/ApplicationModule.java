package com.yafuquen.pagersocket.di;

import android.content.Context;

import com.yafuquen.pagersocket.BuildConfig;
import com.yafuquen.pagersocket.data.publisher.EventPublisher;
import com.yafuquen.pagersocket.data.service.RolesService;
import com.yafuquen.pagersocket.data.service.TeamService;
import com.yafuquen.pagersocket.data.service.TeamUpdateService;
import com.yafuquen.pagersocket.data.service.socket.TeamUpdateServiceImpl;
import com.yafuquen.pagersocket.domain.di.DomainModule;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {

    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    @Named(DomainModule.SUBSCRIBER_SCHEDULER)
    @Singleton
    protected Scheduler providesScheduler() {
        return Schedulers.io();
    }

    @Provides
    @Named(DomainModule.OBSERVER_SCHEDULER)
    @Singleton
    protected Scheduler providesObserverScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Singleton
    protected Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    protected EventPublisher provideEventPublisher() {
        return new EventPublisher();
    }

    @Singleton
    @Provides
    protected TeamService provideTeamService(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(TeamService.class);
    }

    @Singleton
    @Provides
    protected RolesService provideRolesService(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(RolesService.class);
    }

    @Singleton
    @Provides
    protected TeamUpdateService provideTeamUpdateService(OkHttpClient okHttpClient) {
        return new TeamUpdateServiceImpl(BuildConfig.WS_URL, okHttpClient);
    }
}
