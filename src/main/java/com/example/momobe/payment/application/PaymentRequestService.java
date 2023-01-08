package com.example.momobe.payment.application;

import com.example.momobe.payment.dto.PaymentResultDto;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

import static org.springframework.http.MediaType.*;

@Slf4j
@Service
public class PaymentRequestService {
    private final PaymentRestTemplate<PaymentResultDto> restTemplate;
    private final String secretKey;

    public PaymentRequestService(@Value("${payments.toss.secretKey") String secretKey, PaymentRestTemplate<PaymentResultDto> restTemplate) {
        this.secretKey = secretKey + ":";
        this.restTemplate = restTemplate;
    }

    @Transactional
    public PaymentResultDto requestPayment(String paymentKey, String orderId, Long amount) {
        String authKey = generateAuthKey();

        HttpEntity<JSONObject> httpRequestEntity = generateHttpRequestEntity(orderId, amount, authKey);
        ResponseEntity<PaymentResultDto> httpResponseEntity = restTemplate.postHttpRequest(paymentKey, httpRequestEntity);

        return httpResponseEntity.getBody();
    }

    private HttpEntity<JSONObject> generateHttpRequestEntity(String orderId, Long amount, String authKey) {
        HttpHeaders httpHeaders = generateHttpHeaders(authKey);
        JSONObject httpParameter = generateHttpParameters(orderId, amount);

        return new HttpEntity<>(httpParameter, httpHeaders);
    }

    private JSONObject generateHttpParameters(String orderId, Long amount) {
        JSONObject param = new JSONObject();

        param.put("orderId", orderId);
        param.put("amount", amount);

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
