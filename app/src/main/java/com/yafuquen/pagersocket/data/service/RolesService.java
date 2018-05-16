package com.yafuquen.pagersocket.data.service;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RolesService {

    @GET("/roles")
    Observable<Map<String, String>> roles();
}
