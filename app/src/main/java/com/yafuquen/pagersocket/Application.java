package com.yafuquen.pagersocket;

import com.yafuquen.pagersocket.di.ApplicationComponent;
import com.yafuquen.pagersocket.di.ApplicationModule;
import com.yafuquen.pagersocket.di.DaggerApplicationComponent;

public class Application extends android.app.Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    private void initializeInjector() {
        setComponent(DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build());
    }

    public void setComponent(ApplicationComponent component) {
        this.applicationComponent = component;
    }
}
