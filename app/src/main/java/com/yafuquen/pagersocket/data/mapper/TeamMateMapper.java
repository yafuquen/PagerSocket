package com.yafuquen.pagersocket.data.mapper;

import com.yafuquen.pagersocket.data.service.model.TeamMateResponse;
import com.yafuquen.pagersocket.domain.model.TeamMate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class TeamMateMapper {

    @Inject
    public TeamMateMapper() {
        // Constructor for injection
    }

    public List<TeamMate> transform(List<TeamMateResponse> teamMateResponseList, Map<String, String> roleList) {
        List<TeamMate> team = new ArrayList<>();
        for (TeamMateResponse teamMateResponse : teamMateResponseList) {
            String roleId = Integer.toString(teamMateResponse.getRole());
            if (roleList.containsKey(roleId)) {
                team.add(transform(teamMateResponse, roleList.get(roleId)));
            }
        }
        return team;
    }

    private TeamMate transform(TeamMateResponse teamMateResponse, String role) {
        return new TeamMate(teamMateResponse.getName(), teamMateResponse.getAvatar(), teamMateResponse.getGithub(), role,
                teamMateResponse.getGender(), teamMateResponse.getLanguages(), teamMateResponse.getTags(), teamMateResponse.getLocation());
    }
}
