package com.yafuquen.pagersocket.data.cache;

import java.util.Map;

import io.reactivex.Observable;

public class RolesCache {

    private Map<String, String> roles;

    public Observable<Map<String, String>> getRoles() {
        return Observable.defer(() -> {
            if (roles == null) {
                return Observable.empty();
            } else {
                return Observable.just(roles);
            }
        });
    }

    public String getRole(String roleId) {
        if (roles != null && roles.containsKey(roleId)) {
            return roles.get(roleId);
        }
        return "";
    }

    public void put(Map<String, String> roles) {
        this.roles = roles;
    }
}
