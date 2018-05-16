package com.yafuquen.pagersocket.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class User implements Parcelable {

    private final String name;

    private final String avatar;

    @SuppressWarnings("CanBeFinal")
    private final String github;

    private final String role;

    private final String gender;

    private final List<String> languages;

    private final List<String> tags;

    private final String location;

    private String status;

    public User(String name, String avatar, String github, String role, String gender, List<String> languages,
                List<String> tags, String location) {
        this.name = name;
        this.avatar = avatar;
        this.github = github;
        this.role = role;
        this.gender = gender;
        this.languages = languages;
        this.tags = tags;
        this.location = location;
        status = "";
    }

    private User(Parcel in) {
        name = in.readString();
        avatar = in.readString();
        github = in.readString();
        role = in.readString();
        gender = in.readString();
        languages = in.createStringArrayList();
        tags = in.createStringArrayList();
        location = in.readString();
        status = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(avatar);
        dest.writeString(github);
        dest.writeString(role);
        dest.writeString(gender);
        dest.writeStringList(languages);
        dest.writeStringList(tags);
        dest.writeString(location);
        dest.writeString(status);
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getGithub() {
        return github;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getLocation() {
        return location;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }
}
