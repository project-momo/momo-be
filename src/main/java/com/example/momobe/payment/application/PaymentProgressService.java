package com.example.momobe.payment.application;

import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.domain.enums.PayState;
import com.example.momobe.payment.dto.PaymentResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentProgressService {
    private final PaymentCommonService paymentCommonService;
    private final PaymentVerificationService paymentVerificationService;
    private final PaymentRequestService paymentRequestService;
    private final PaymentSuccessService paymentSuccessService;

    @Transactional
    public PaymentResultDto progress(String orderId, String paymentKey, Long amount) {
        Payment payment = paymentCommonService.getPayment(orderId);

        if (paymentVerificationService.verify(amount, payment)) payment.setPaymentKey(paymentKey);

        Map<String, Object> map = createMap(orderId, amount);
        PaymentResultDto paymentResponse = paymentRequestService.process(paymentKey, map);
        paymentSuccessService.setSuccessState(orderId);

        return paymentResponse;
    }

    private Map<String, Object> createMap(String orderId, Long amount) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("amount", amount);
        return map;
    }
}
