package com.yafuquen.pagersocket.data.service;

import com.yafuquen.pagersocket.data.service.model.TeamMateResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface TeamService {

    @GET("/team")
    Observable<List<TeamMateResponse>> team();
}
