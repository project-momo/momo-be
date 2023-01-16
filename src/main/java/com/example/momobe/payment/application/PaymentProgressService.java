package com.example.momobe.payment.application;

import com.example.momobe.payment.domain.Payment;
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

    @Transactional
    public PaymentResultDto progress(String orderId, String paymentKey, Long amount) {
        Payment payment = paymentCommonService.getPaymentOrThrowException(orderId);

        if (paymentVerificationService.verify(amount, payment)) payment.setPaymentKey(paymentKey);

        Map<String, Object> map = createMap(orderId, amount);
        return paymentRequestService.process(paymentKey, map);
    }

    private Map<String, Object> createMap(String orderId, Long amount) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("amount", amount);
        return map;
    }
}
