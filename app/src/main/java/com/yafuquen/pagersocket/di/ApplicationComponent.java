package com.yafuquen.pagersocket.di;

import com.yafuquen.pagersocket.data.di.DataModule;
import com.yafuquen.pagersocket.domain.di.DomainModule;
import com.yafuquen.pagersocket.view.di.TeamComponent;
import com.yafuquen.pagersocket.view.di.TeamModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, DataModule.class, DomainModule.class})
public interface ApplicationComponent {

    TeamComponent teamComponent(TeamModule teamModule);
}
