package com.example.momobe.payment.application;

import com.amazonaws.util.Base64;
import com.example.momobe.payment.domain.UnableProceedPaymentException;
import com.example.momobe.payment.dto.PaymentResultDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import static com.example.momobe.common.exception.enums.ErrorCode.*;
import static org.springframework.http.MediaType.*;

@Slf4j
@Service
public class PaymentRequestService {
    private final String TOSS_URL = "https://api.tosspayments.com/v1/payments/";
    private final String secretKey;

    public PaymentRequestService(@Value("${payments.toss.secretKey") String secretKey) {
        this.secretKey = secretKey + ":";
    }

    @Transactional
    public PaymentResultDto requestPayment(String paymentKey, String orderId, Long amount) {
        String authKey = generateAuthKey();

        HttpEntity<JSONObject> httpRequestEntity = generateHttpRequestEntity(orderId, amount, authKey);
        ResponseEntity<PaymentResultDto> httpResponseEntity = postHttpRequest(paymentKey, httpRequestEntity);

        return httpResponseEntity.getBody();
    }

    private HttpEntity<JSONObject> generateHttpRequestEntity(String orderId, Long amount, String authKey) {
        HttpHeaders httpHeaders = generateHttpHeaders(authKey);
        JSONObject httpParameter = generateHttpParameters(orderId, amount);

        return new HttpEntity<>(httpParameter, httpHeaders);
    }

    private ResponseEntity<PaymentResultDto> postHttpRequest(String paymentKey, HttpEntity<JSONObject> httpEntity) {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForEntity(
                TOSS_URL + paymentKey,
                httpEntity,
                PaymentResultDto.class
        );
    }

    private JSONObject generateHttpParameters(String orderId, Long amount) {
        JSONObject param = new JSONObject();

        try {
            param.put("orderId", orderId);
            param.put("amount", amount);
        } catch (JSONException e) {
            throw new UnableProceedPaymentException(UNABLE_TO_PROCESS);
        }

        return param;
    }

    private HttpHeaders generateHttpHeaders(String authKey) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setBasicAuth(authKey);
        httpHeaders.setConnection(APPLICATION_JSON_VALUE);
        httpHeaders.setAccept(Collections.singletonList(APPLICATION_JSON));

        return httpHeaders;
    }

    private String generateAuthKey() {
        return Arrays.toString(Base64.encode(secretKey.getBytes(StandardCharsets.UTF_8)));
    }
}
