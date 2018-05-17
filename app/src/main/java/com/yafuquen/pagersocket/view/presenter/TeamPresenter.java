package com.yafuquen.pagersocket.view.presenter;

import com.yafuquen.pagersocket.domain.interactor.TeamInteractor;
import com.yafuquen.pagersocket.domain.model.TeamMate;
import com.yafuquen.pagersocket.domain.model.event.Event;
import com.yafuquen.pagersocket.domain.model.event.StateEvent;
import com.yafuquen.pagersocket.domain.model.event.TeamMateEvent;
import com.yafuquen.pagersocket.view.mapper.UserMapper;
import com.yafuquen.pagersocket.view.model.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class TeamPresenter extends Presenter {

    private final TeamInteractor teamInteractor;

    private final UserMapper userMapper;

    private View view;

    @Inject
    public TeamPresenter(TeamInteractor teamInteractor, UserMapper userMapper) {
        this.teamInteractor = teamInteractor;
        this.userMapper = userMapper;
    }

    public void loadList() {
        teamInteractor.execute(new DisposableObserver<List<TeamMate>>() {
            @Override
            public void onNext(List<TeamMate> teamMates) {
                if (isViewReady()) {
                    view.showList(userMapper.transform(teamMates));
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            protected void onStart() {
                if (isViewReady()) {
                    view.showLoading();
                }
            }
        });
    }

    public void requestUpdates() {
        teamInteractor.requestUpdates(this::handleEvent);
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    protected Presenter.View getView() {
        return view;
    }

    @Override
    public void destroy() {
        view = null;
        teamInteractor.dispose();
    }

    private void handleEvent(Event teamEvent) {
        if (teamEvent instanceof TeamMateEvent) {
            showTeamMate(((TeamMateEvent) teamEvent).getTeamMate());
        } else if (teamEvent instanceof StateEvent) {
            StateEvent stateEvent = (StateEvent) teamEvent;
            showState(stateEvent.getUser(), stateEvent.getState());
        }
    }

    private void showState(String username, String state) {
        if (isViewReady()) {
            view.showState(username, state);
        }
    }

    private void showTeamMate(TeamMate teamMate) {
        if (isViewReady()) {
            view.showUser(userMapper.transform(teamMate));
        }
    }

    public interface View extends Presenter.View {

        void showList(List<User> users);

        void showState(String username, String state);

        void showUser(User user);

        void showLoading();
    }
}
