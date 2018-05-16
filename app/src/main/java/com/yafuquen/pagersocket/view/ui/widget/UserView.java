package com.yafuquen.pagersocket.view.ui.widget;

import android.content.Context;
import android.support.text.emoji.EmojiCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yafuquen.pagersocket.R;
import com.yafuquen.pagersocket.view.model.User;
import com.yafuquen.pagersocket.view.transform.CircleTransform;
import com.yafuquen.pagersocket.view.ui.util.DisplayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserView extends RelativeLayout {

    @BindView(R.id.user_name)
    TextView name;

    @BindView(R.id.user_role)
    TextView role;

    @BindView(R.id.user_github)
    TextView github;

    @BindView(R.id.user_avatar)
    ImageView avatar;

    @BindView(R.id.user_location)
    TextView location;

    @BindView(R.id.user_languages)
    TextView languages;

    @BindView(R.id.user_tags)
    TextView tags;

    public UserView(Context context) {
        super(context);
    }

    public UserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void bindTo(User user, Picasso picasso) {
        name.setText(user.getName());
        role.setText(user.getRole());
        github.setText(user.getGithub());
        location.setText(DisplayUtils.getCountry(user.getLocation()));
        languages.setText(DisplayUtils.getLanguages(user.getLanguages()));
        tags.setText(EmojiCompat.get().process(TextUtils.join(", ", user.getTags())));
        picasso.load(user.getAvatar()).transform(new CircleTransform()).into(avatar);
    }

}
