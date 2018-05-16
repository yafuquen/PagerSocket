package com.yafuquen.pagersocket.view.mapper;

import com.yafuquen.pagersocket.domain.model.TeamMate;
import com.yafuquen.pagersocket.view.model.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class UserMapper {

    @Inject
    public UserMapper() {
        // Constructor for injection
    }

    public List<User> transform(List<TeamMate> teamMates) {
        List<User> users = new ArrayList<>();
        for (TeamMate teamMate : teamMates) {
            users.add(transform(teamMate));
        }
        return users;
    }

    public User transform(TeamMate teamMate) {
        return new User(teamMate.getName(), teamMate.getAvatar(), teamMate.getGithub(), teamMate.getRole(),
                teamMate.getGender(), teamMate.getLanguages(), teamMate.getTags(), teamMate.getLocation());
    }
}
