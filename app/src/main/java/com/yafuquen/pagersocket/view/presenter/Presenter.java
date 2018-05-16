package com.yafuquen.pagersocket.view.presenter;

public abstract class Presenter {

    boolean isViewReady() {
        return getView() != null && getView().isReady();
    }

    abstract View getView();

    abstract void destroy();

    public interface View {

        boolean isReady();
    }
}
