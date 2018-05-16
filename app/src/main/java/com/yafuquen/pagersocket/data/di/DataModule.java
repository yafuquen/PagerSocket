package com.yafuquen.pagersocket.data.di;

import com.google.gson.Gson;
import com.yafuquen.pagersocket.data.cache.RolesCache;
import com.yafuquen.pagersocket.data.repository.TeamRepositoryImpl;
import com.yafuquen.pagersocket.domain.repository.TeamRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class DataModule {

    @Singleton
    @Provides
    OkHttpClient okHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @Singleton
    @Provides
    TeamRepository provideTeamRepository(TeamRepositoryImpl teamRepository) {
        return teamRepository;
    }

    @Singleton
    @Provides
    Gson provideGson() {
        return new Gson();
    }

    @Singleton
    @Provides
    RolesCache provideRolesCache() {
        return new RolesCache();
    }
}
