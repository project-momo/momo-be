package com.example.momobe.payment.application;

import com.example.momobe.common.application.ApiService;
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
public class PaymentRequestService extends PaymentApiService<PaymentResultDto> {
    public PaymentRequestService(@Value("${payments.toss.secretKey") String secretKey, @Value("${payments.toss.url}") String tossUrl, ApiService<PaymentResultDto> apiService) {
        super(secretKey, tossUrl, apiService);
    }

    @Override
    protected PaymentResultDto requestApi(String paymentKey, HttpEntity<JSONObject> httpRequestEntity) {
        return apiService.post(tossUrl + paymentKey, httpRequestEntity, PaymentResultDto.class).getBody();
    }
}
