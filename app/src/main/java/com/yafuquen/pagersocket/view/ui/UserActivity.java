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

    private static final String EDITING_STATUS = "editingStatus";

    private static final String USER = "user";

    @BindView(R.id.user_layout)
    UserView userView;

    @BindView(R.id.user_status)
    TextView status;

    @BindView(R.id.user_status_edit)
    EditText statusEdit;

    @BindView(R.id.status_edit_layout)
    View editLayout;

    @BindView(R.id.status_save_layout)
    View saveLayout;

    @BindView(R.id.user_status_edit_action)
    ImageButton editButton;

    @Inject
    Picasso picasso;

    @Inject
    UserPresenter userPresenter;

    private boolean editing;

    @OnClick(R.id.user_status_edit_action)
    void editStatus() {
        showApplyStatus();
    }

    @OnClick(R.id.user_status_cancel_action)
    void cancelEditStatus() {
        showEditStatus();
    }

    @OnClick(R.id.user_status_apply_action)
    void applyEditStatus() {
        saveStatus();
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
    protected void initializeInjector() {
        TeamComponent teamComponent = getApplicationComponent().teamComponent(new TeamModule());
        teamComponent.inject(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EDITING_STATUS, editing);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        userView.bindTo(userPresenter.getUser(), picasso);
        showState(userPresenter.getUser().getStatus());
        if (getIntent().getBooleanExtra(EDITING_STATUS, false)) {
            showApplyStatus();
        } else {
            showEditStatus();
        }
    }

    private void saveStatus() {
        String status = statusEdit.getText().toString();
        if (!TextUtils.isEmpty(status)) {
            userPresenter.updateStatus(status);
        }
    }

    private void showApplyStatus() {
        editing = true;
        editLayout.setVisibility(View.GONE);
        saveLayout.setVisibility(View.VISIBLE);
        statusEdit.requestFocus();
        statusEdit.setSelection(statusEdit.getText().length());
    }

    private void showEditStatus() {
        editing = false;
        saveLayout.setVisibility(View.GONE);
        editLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void statusUpdateError() {
        Toast.makeText(this, R.string.status_update_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void statusUpdated() {
        editing = false;
        finish();
    }

    @Override
    public void showState(String state) {
        if (editing) {
            return;
        }
        if (!TextUtils.isEmpty(state)) {
            status.setText(EmojiCompat.get().process(state));
            statusEdit.setText(EmojiCompat.get().process(state));
        } else {
            status.setText(R.string.no_status);
        }
    }

    @Override
    public void showUser(User updatedUser) {
        updatedUser.setStatus(userPresenter.getUser().getStatus());
        userPresenter.setUser(updatedUser);
        getIntent().putExtra(USER, updatedUser);
    }

    @Override
    public void onConnectionError() {
        editButton.setEnabled(false);
    }
}
