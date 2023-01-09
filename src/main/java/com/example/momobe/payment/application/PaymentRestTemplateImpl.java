package com.example.momobe.payment.application;

import com.example.momobe.payment.dto.PaymentResultDto;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PaymentRestTemplateImpl implements PaymentRestTemplate<PaymentResultDto> {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String TOSS_URL = "https://api.tosspayments.com/v1/payments/";

    @Override
    public ResponseEntity<PaymentResultDto> postHttpRequest(String paymentKey, HttpEntity<JSONObject> httpEntity) {
        return restTemplate.postForEntity(TOSS_URL, httpEntity, PaymentResultDto.class);
    }
}
