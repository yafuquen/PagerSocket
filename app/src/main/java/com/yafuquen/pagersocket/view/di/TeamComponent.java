package com.yafuquen.pagersocket.view.di;

import com.yafuquen.pagersocket.view.ui.TeamActivity;
import com.yafuquen.pagersocket.view.ui.UserActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {TeamModule.class, ViewModule.class})
public interface TeamComponent {

    void inject(TeamActivity teamActivity);

    void inject(UserActivity userActivity);
}
