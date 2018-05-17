package com.yafuquen.pagersocket.view.ui.adapter;

import android.support.annotation.NonNull;
import android.support.text.emoji.EmojiCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yafuquen.pagersocket.R;
import com.yafuquen.pagersocket.view.model.User;
import com.yafuquen.pagersocket.view.ui.widget.UserView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.UserHolder> {

    private final Picasso picasso;

    private final OnUserClickListener onUserClickListener;

    private List<User> users = Collections.emptyList();

    public TeamAdapter(Picasso picasso, OnUserClickListener onUserClickListener) {
        this.picasso = picasso;
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void updateState(String username, String state) {
        int position = getPosition(username);
        if (position >= 0) {
            users.get(position).setState(state);
            notifyItemChanged(position);
        }
    }

    public void showUser(User updatedUser) {
        int position = getPosition(updatedUser);
        if (position < 0) {
            users.add(updatedUser);
            notifyItemInserted(users.size());
        } else {
            User user = users.get(position);
            updatedUser.setState(user.getState());
            users.set(position, updatedUser);
            notifyItemChanged(position);
        }
    }

    private int getPosition(User updatedUser) {
        return getPosition(updatedUser.getGithub());
    }

    private int getPosition(String username) {
        int position = 0;
        for (User user : users) {
            if (user.getGithub().equals(username)) {
                return position;
            }
            position++;
        }
        return -1;
    }

    class UserHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_layout)
        UserView userView;

        @BindView(R.id.user_card)
        CardView userCard;

        @BindView(R.id.user_state)
        TextView state;

        private UserHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(User user) {
            userView.bindTo(user, picasso);
            userCard.setOnClickListener(v -> {
                if (onUserClickListener != null) {
                    onUserClickListener.onClick(user);
                }
            });
            showState(user.getState());
        }

        private void showState(String userState) {
            if (!TextUtils.isEmpty(userState)) {
                state.setText(EmojiCompat.get().process(userState));
            } else {
                state.setText(R.string.no_state);
            }
        }
    }

    public interface OnUserClickListener {
        void onClick(User user);
    }
}
