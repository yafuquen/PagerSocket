package com.yafuquen.pagersocket.view.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yafuquen.pagersocket.Application;
import com.yafuquen.pagersocket.di.ApplicationComponent;
import com.yafuquen.pagersocket.view.presenter.Presenter;

abstract class BaseActivity extends AppCompatActivity implements Presenter.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject();
    }

    ApplicationComponent getApplicationComponent() {
        return getApplicationInstance().getApplicationComponent();
    }

    @Override
    public boolean isReady() {
        return !isFinishing();
    }

    private Application getApplicationInstance() {
        return (Application) getApplication();
    }

    protected abstract void inject();
}
