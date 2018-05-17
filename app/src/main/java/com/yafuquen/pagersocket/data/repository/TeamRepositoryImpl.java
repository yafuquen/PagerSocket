package com.yafuquen.pagersocket.data.repository;

import com.yafuquen.pagersocket.data.cache.RolesCache;
import com.yafuquen.pagersocket.data.mapper.EventMapper;
import com.yafuquen.pagersocket.data.mapper.TeamMateMapper;
import com.yafuquen.pagersocket.data.service.RolesService;
import com.yafuquen.pagersocket.data.service.TeamService;
import com.yafuquen.pagersocket.data.service.TeamUpdateService;
import com.yafuquen.pagersocket.domain.model.TeamMate;
import com.yafuquen.pagersocket.domain.model.event.Event;
import com.yafuquen.pagersocket.domain.repository.TeamRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

public class TeamRepositoryImpl implements TeamRepository {

    private final TeamService teamService;

    private final RolesService rolesService;

    private final TeamUpdateService teamUpdateService;

    private final TeamMateMapper teamMateMapper;

    private final EventMapper eventMapper;

    private final RolesCache rolesCache;

    @Inject
    public TeamRepositoryImpl(TeamService teamService, RolesService rolesService,
                              TeamUpdateService teamUpdateService, TeamMateMapper teamMateMapper,
                              EventMapper eventMapper, RolesCache rolesCache) {
        this.teamService = teamService;
        this.rolesService = rolesService;
        this.teamUpdateService = teamUpdateService;
        this.teamMateMapper = teamMateMapper;
        this.eventMapper = eventMapper;
        this.rolesCache = rolesCache;
    }

    @Override
    public Observable<List<TeamMate>> get() {
        return teamService.team().zipWith(getRoles(), teamMateMapper::transform);
    }

    @Override
    public Observable<Void> updateState(String username, String newState) {
        return teamUpdateService.updateState(eventMapper.buildMessage(username, newState));
    }

    @Override
    public Observable<Event> receiveUpdates() {
        return teamUpdateService.receiveEvents().map(eventMapper::transform);
    }

    private Observable<Map<String, String>> getRoles() {
        return Observable.concat(rolesCache.getRoles(), rolesService.roles()).take(1).doOnNext(rolesCache::put);
    }
}
