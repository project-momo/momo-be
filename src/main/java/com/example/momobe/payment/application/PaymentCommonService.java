package com.example.momobe.payment.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.domain.PaymentRepository;
import com.example.momobe.payment.domain.UnableProceedPaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentCommonService {
    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public Payment getPayment(String paymentId) {
        return paymentRepository.findPaymentByOrderId(paymentId).orElseThrow(() -> new UnableProceedPaymentException(ErrorCode.DATA_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Payment getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() -> new UnableProceedPaymentException(ErrorCode.DATA_NOT_FOUND));
    }
}
