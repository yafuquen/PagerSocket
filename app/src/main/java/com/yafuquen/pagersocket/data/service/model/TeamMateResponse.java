package com.yafuquen.pagersocket.data.service.model;

import java.util.List;

public class TeamMateResponse {

    private final String name;

    private final String avatar;

    private final String github;

    private final int role;

    private final String gender;

    private final List<String> languages;

    private final List<String> tags;

    private final String location;

    public TeamMateResponse(String name, String avatar, String github, int role, String gender, List<String> languages, List<String> tags, String location) {
        this.name = name;
        this.avatar = avatar;
        this.github = github;
        this.role = role;
        this.gender = gender;
        this.languages = languages;
        this.tags = tags;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getGithub() {
        return github;
    }

    public int getRole() {
        return role;
    }

    public String getGender() {
        return gender;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getLocation() {
        return location;
    }
}
