package com.online.foodstore.config;

import org.springframework.http.HttpMethod;

import java.util.*;

public interface OpenEndpoints {


    Map<HttpMethod, List<String>> OPEN_ENDPOINTS = Map.of(
            HttpMethod.POST, List.of(),
            HttpMethod.DELETE, List.of(),
            HttpMethod.GET, List.of(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/swagger-resources"));

    default List<String> getByMethod(HttpMethod method) {
        var endpoints = OPEN_ENDPOINTS.get(method);
        return Objects.nonNull(endpoints) ? endpoints : Collections.emptyList();
    }

    default String[] getByMethodArr(HttpMethod method) {
        return getByMethod(method).toArray(new String[]{});
    }

    default String[] getPostOpenEndpoints() {
        return getByMethodArr(HttpMethod.POST);
    }

    default String[] getGetOpenEndpoints() {
        return getByMethodArr(HttpMethod.GET);
    }

    default String[] getDeleteOpenEndpoints() {
        return getByMethodArr(HttpMethod.DELETE);
    }

    default List<String> whiteList() {
        return OPEN_ENDPOINTS.values().stream().flatMap(Collection::stream).toList();
    }
}
