package com.example.momobe.payment.application;

import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.dto.PaymentResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentProgressService {
    private final PaymentCommonService paymentCommonService;
    private final PaymentVerificationService paymentVerificationService;
    private final PaymentRequestService paymentRequestService;

    @Transactional
    public PaymentResultDto progress(String orderId, String paymentKey, Long amount) {
        Payment payment = paymentCommonService.getPaymentOrThrowException(orderId);

        if (paymentVerificationService.verify(orderId, paymentKey, amount, payment)) payment.setPaymentKey(paymentKey);

        return paymentRequestService.requestPayment(paymentKey, orderId, amount);
    }
}
