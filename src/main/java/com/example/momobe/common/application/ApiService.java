package com.example.momobe.common.application;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiService<T> {
    private final RestTemplate restTemplate;

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<T> post(String url, HttpEntity<JSONObject> httpHeaders, Class<T> type) {
        return restTemplate.postForEntity(url, httpHeaders, type);
    }
}
