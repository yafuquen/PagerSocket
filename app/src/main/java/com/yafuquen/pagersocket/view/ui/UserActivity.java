package com.yafuquen.pagersocket.view.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.text.emoji.EmojiCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yafuquen.pagersocket.R;
import com.yafuquen.pagersocket.view.di.TeamComponent;
import com.yafuquen.pagersocket.view.di.TeamModule;
import com.yafuquen.pagersocket.view.model.User;
import com.yafuquen.pagersocket.view.presenter.UserPresenter;
import com.yafuquen.pagersocket.view.ui.widget.UserView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserActivity extends BaseActivity implements UserPresenter.View {

    private static final String EDITING_STATE = "editingState";

    private static final String USER = "user";

    @BindView(R.id.user_layout)
    UserView userView;

    @BindView(R.id.user_state)
    TextView stateText;

    @BindView(R.id.user_state_edit)
    EditText stateEdit;

    @BindView(R.id.state_edit_layout)
    View editLayout;

    @BindView(R.id.state_save_layout)
    View saveLayout;

    @BindView(R.id.user_state_edit_action)
    ImageButton editButton;

    @Inject
    Picasso picasso;

    @Inject
    UserPresenter userPresenter;

    private boolean editing;

    @OnClick(R.id.user_state_edit_action)
    void editState() {
        showApplyState();
    }

    @OnClick(R.id.user_state_cancel_action)
    void cancelEditState() {
        showEditState();
    }

    @OnClick(R.id.user_state_apply_action)
    void applyEditState() {
        saveState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        userPresenter.setView(this);
        User user = getIntent().getParcelableExtra(USER);
        if (user != null) {
            userPresenter.setUser(user);
            ButterKnife.bind(this);
            setupView();
        } else {
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userPresenter.destroy();
    }

    public static Intent getCallingIntent(User user, Activity activity) {
        Intent intent = new Intent(activity, UserActivity.class);
        intent.putExtra(USER, user);
        return intent;
    }

    @Override
    protected void inject() {
        TeamComponent teamComponent = getApplicationComponent().teamComponent(new TeamModule());
        teamComponent.inject(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDITING_STATE, editing);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void stateUpdateError() {
        Toast.makeText(this, R.string.state_update_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void stateUpdated() {
        editing = false;
        finish();
    }

    @Override
    public void showState(String state) {
        if (editing) {
            return;
        }
        if (!TextUtils.isEmpty(state)) {
            stateText.setText(EmojiCompat.get().process(state));
            stateEdit.setText(EmojiCompat.get().process(state));
        } else {
            this.stateText.setText(R.string.no_state);
        }
    }

    @Override
    public void showUser(User updatedUser) {
        updatedUser.setState(userPresenter.getUser().getState());
        userPresenter.setUser(updatedUser);
        getIntent().putExtra(USER, updatedUser);
    }

    @Override
    public void onConnectionError() {
        editButton.setEnabled(false);
    }

    private void setupView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        userView.bindTo(userPresenter.getUser(), picasso);
        showState(userPresenter.getUser().getState());
        if (getIntent().getBooleanExtra(EDITING_STATE, false)) {
            showApplyState();
        } else {
            showEditState();
        }
    }

    private void saveState() {
        String state = stateEdit.getText().toString();
        if (!TextUtils.isEmpty(state)) {
            userPresenter.updateState(state);
        }
    }

    private void showApplyState() {
        editing = true;
        editLayout.setVisibility(View.GONE);
        saveLayout.setVisibility(View.VISIBLE);
        stateEdit.requestFocus();
        stateEdit.setSelection(stateEdit.getText().length());
    }

    private void showEditState() {
        editing = false;
        saveLayout.setVisibility(View.GONE);
        editLayout.setVisibility(View.VISIBLE);
    }
}
