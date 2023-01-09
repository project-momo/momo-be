package com.example.momobe.payment.application;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface PaymentRestTemplate <T> {
    ResponseEntity<T> postHttpRequest(String paymentKey, HttpEntity<JSONObject> httpEntity);
}
