package com.example.momobe.payment.application;

import com.example.momobe.common.application.ApiService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public abstract class PaymentApiService <T> {
    protected ApiService<T> apiService;
    protected String tossUrl;
    protected String secretKey;

    protected PaymentApiService(@Value("${payments.toss.secretKey") String secretKey, @Value("${payments.toss.url") String tossUrl, ApiService<T> apiService) {
        this.secretKey = secretKey + ":";
        this.tossUrl = tossUrl;
        this.apiService = apiService;
    }

    @Transactional
    public T process(String paymentKey, Map<String, Object> paramMap) {
        String authKey = generateAuthKey();

        HttpEntity<JSONObject> httpRequestEntity = generateHttpRequestEntity(paramMap, authKey);
        return requestApi(paymentKey, httpRequestEntity);
    }

    protected abstract T requestApi(String paymentKey, HttpEntity<JSONObject> httpRequestEntity);

    private HttpEntity<JSONObject> generateHttpRequestEntity(Map<String, Object> paramMap, String authKey) {
        HttpHeaders httpHeaders = generateHttpHeaders(authKey);
        JSONObject httpParameter = generateHttpParameters(paramMap);

        return new HttpEntity<>(httpParameter, httpHeaders);
    }

    private JSONObject generateHttpParameters(Map<String, Object> paramMap) {
        JSONObject param = new JSONObject();
        param.putAll(paramMap);

        return param;
    }

    private HttpHeaders generateHttpHeaders(String authKey) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setBasicAuth(authKey);
        httpHeaders.setContentType(APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(APPLICATION_JSON));

        return httpHeaders;
    }

    private String generateAuthKey() {
        return Arrays.toString(Base64.getEncoder().encode(secretKey.getBytes(StandardCharsets.UTF_8)));
    }
}
