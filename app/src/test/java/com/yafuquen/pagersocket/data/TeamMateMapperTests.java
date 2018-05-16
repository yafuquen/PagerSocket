package com.yafuquen.pagersocket.data;

import com.yafuquen.pagersocket.data.mapper.TeamMateMapper;
import com.yafuquen.pagersocket.data.service.model.TeamMateResponse;
import com.yafuquen.pagersocket.domain.model.TeamMate;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TeamMateMapperTests {

    private final TeamMateMapper teamMateMapper = new TeamMateMapper();

    @Test
    public void transform() {
        List<TeamMateResponse> teamMateResponseList = createList();
        Map<String, String> roles = createRoles();
        List<TeamMate> teamMates = teamMateMapper.transform(teamMateResponseList, roles);
        for (int index = 0; index < teamMates.size(); index++) {
            assertEquals(teamMateResponseList.get(index).getName(), teamMates.get(index).getName());
            assertEquals(teamMateResponseList.get(index).getAvatar(), teamMates.get(index).getAvatar());
            assertEquals(teamMateResponseList.get(index).getGithub(), teamMates.get(index).getGithub());
            assertEquals(teamMateResponseList.get(index).getGender(), teamMates.get(index).getGender());
            assertEquals(teamMateResponseList.get(index).getLocation(), teamMates.get(index).getLocation());
            assertEquals(roles.get(Integer.toString(teamMateResponseList.get(index).getRole())), teamMates.get(index).getRole());
        }

    }

    @Test
    public void transformNoRole() {
        List<TeamMateResponse> teamMateResponseList = createList();
        Map<String, String> roles = new HashMap<>();
        List<TeamMate> teamMates = teamMateMapper.transform(teamMateResponseList, roles);
        assertTrue(teamMates.isEmpty());
    }

    private List<TeamMateResponse> createList() {
        List<TeamMateResponse> teamMateResponseList = new ArrayList<>();
        teamMateResponseList.add(new TeamMateResponse("Name", "avatar", "github", 1,
                "Male", new ArrayList<>(), new ArrayList<>(), "us"));
        return teamMateResponseList;
    }

    private Map<String, String> createRoles() {
        Map<String, String> roles = new HashMap<>();
        roles.put("1", "role");
        return roles;
    }
}
