package com.yafuquen.pagersocket.view.presenter;

import com.yafuquen.pagersocket.domain.interactor.TeamInteractor;
import com.yafuquen.pagersocket.domain.model.TeamMate;
import com.yafuquen.pagersocket.domain.model.event.Event;
import com.yafuquen.pagersocket.domain.model.event.NoConnectionEvent;
import com.yafuquen.pagersocket.domain.model.event.StateEvent;
import com.yafuquen.pagersocket.domain.model.event.TeamMateEvent;
import com.yafuquen.pagersocket.view.mapper.UserMapper;
import com.yafuquen.pagersocket.view.model.User;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class UserPresenter extends Presenter {

    private final TeamInteractor teamInteractor;

    private final UserMapper userMapper;

    private View view;

    private User user;

    @Inject
    public UserPresenter(TeamInteractor teamInteractor, UserMapper userMapper) {
        this.teamInteractor = teamInteractor;
        this.userMapper = userMapper;
    }

    public void setView(View view) {
        this.view = view;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        teamInteractor.requestUpdates(this::handleEvent, event -> {
            if (event instanceof StateEvent) {
                return ((StateEvent) event).getUser().equals(user.getGithub());
            } else if (event instanceof TeamMateEvent) {
                return ((TeamMateEvent) event).getTeamMate().getGithub().equals(user.getGithub());
            }
            return false;
        });
    }

    public void updateState(String state) {
        teamInteractor.updateState(user.getGithub(), state, new DisposableObserver<Void>() {
            @Override
            public void onNext(Void aVoid) {
            }

            @Override
            public void onError(Throwable e) {
                if (isViewReady()) {
                    view.stateUpdateError();
                }
            }

            @Override
            public void onComplete() {
                user.setState(state);
                if (isViewReady()) {
                    view.stateUpdated();
                }
            }
        });
    }

    @Override
    protected View getView() {
        return view;
    }

    @Override
    public void destroy() {
        view = null;
        teamInteractor.dispose();
    }

    private void handleEvent(Event event) {
        if (event instanceof TeamMateEvent) {
            showTeamMate(((TeamMateEvent) event).getTeamMate());
        } else if (event instanceof StateEvent) {
            showState(((StateEvent) event).getState());
        } else if (event instanceof NoConnectionEvent) {
            onConnectionError();
        }
    }

    private void onConnectionError() {
        if (isViewReady()) {
            view.onConnectionError();
        }
    }

    private void showState(String state) {
        if (isViewReady()) {
            view.showState(state);
        }
    }

    private void showTeamMate(TeamMate teamMate) {
        if (isViewReady()) {
            view.showUser(userMapper.transform(teamMate));
        }
    }

    public interface View extends Presenter.View {

        void stateUpdateError();

        void stateUpdated();

        void showState(String state);

        void showUser(User updatedUser);

        void onConnectionError();
    }
}
