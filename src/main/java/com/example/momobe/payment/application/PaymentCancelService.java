package com.example.momobe.payment.application;


import com.example.momobe.common.application.ApiService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;


@Service
public class PaymentCancelService extends PaymentApiService<Void> {
    public PaymentCancelService(@Value("${payments.toss.secretKey}") String secretKey, @Value("${payments.toss.url}") String tossUrl, ApiService<Void> apiService) {
        super(secretKey, tossUrl, apiService);
    }

    @Override
    protected Void requestApi(String paymentKey, HttpEntity<JSONObject> httpRequestEntity) {
        return apiService.post(paymentKey, httpRequestEntity, Void.class).getBody();
    }
}
