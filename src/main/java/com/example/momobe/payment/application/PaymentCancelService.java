package com.example.momobe.payment.application;


import com.example.momobe.common.application.ApiService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON;


@Service
public class PaymentCancelService {
    private final String secretKey;
    private final String tossUrl;
    private final ApiService<String> apiService;

    public PaymentCancelService(@Value("${payments.toss.secretKey}") String secretKey, @Value("${payments.toss.url}") String tossUrl, ApiService<String> apiService) {
        this.secretKey = secretKey;
        this.tossUrl = tossUrl;
        this.apiService = apiService;
    }

    @Transactional
    public void cancelPayment(String paymentKey, String reason) {
        String authKey = generateAuthKey();
        HttpEntity<JSONObject> httpRequestEntity = generateHttpRequestEntity(authKey, reason);
        apiService.post(tossUrl + paymentKey + "/cancel", httpRequestEntity, String.class);
    }

    private HttpEntity<JSONObject> generateHttpRequestEntity(String authKey, String reason) {
        HttpHeaders httpHeaders = generateHttpHeaders(authKey);
        JSONObject httpParameter = generateHttpParameters(reason);

        return new HttpEntity<>(httpParameter, httpHeaders);
    }


    private JSONObject generateHttpParameters(String reason) {
        JSONObject param = new JSONObject();
        param.put("cancelReason", reason);

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
