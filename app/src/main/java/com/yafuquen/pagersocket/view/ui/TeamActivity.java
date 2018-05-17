package com.yafuquen.pagersocket.view.ui;

import android.os.Bundle;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.bundled.BundledEmojiCompatConfig;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.yafuquen.pagersocket.R;
import com.yafuquen.pagersocket.view.di.TeamComponent;
import com.yafuquen.pagersocket.view.di.TeamModule;
import com.yafuquen.pagersocket.view.model.User;
import com.yafuquen.pagersocket.view.presenter.TeamPresenter;
import com.yafuquen.pagersocket.view.ui.adapter.TeamAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamActivity extends BaseActivity implements TeamPresenter.View {

    @Inject
    TeamPresenter teamPresenter;

    @Inject
    Picasso picasso;

    @BindView(R.id.team_list)
    RecyclerView teamList;

    @BindView(R.id.loading_layout)
    View loading;

    private TeamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        ButterKnife.bind(this);
        setupView();
        teamPresenter.setView(this);
        teamPresenter.loadList();
    }

    @Override
    public void showList(List<User> users) {
        adapter.setUsers(users);
        loading.setVisibility(View.GONE);
    }

    @Override
    public void showState(String username, String state) {
        adapter.updateState(username, state);
    }

    @Override
    public void showUser(User user) {
        adapter.showUser(user);
    }

    @Override
    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    protected void inject() {
        TeamComponent teamComponent = getApplicationComponent().teamComponent(new TeamModule());
        teamComponent.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        teamPresenter.destroy();
    }

    private void setupView() {
        EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        EmojiCompat.init(config);
        adapter = new TeamAdapter(picasso, this::showDetail);
        teamList.setLayoutManager(new LinearLayoutManager(this));
        teamList.setAdapter(adapter);
    }

    private void showDetail(User user) {
        startActivity(UserActivity.getCallingIntent(user, this));
    }
}
