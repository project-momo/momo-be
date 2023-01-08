package com.example.momobe.payment.application;

import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.domain.PaymentRepository;
import com.example.momobe.reservation.dto.out.ReservationPaymentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.example.momobe.payment.domain.enums.PayType.*;

@Service
public class PaymentSaveService {
    private final PaymentRepository paymentRepository;

    private final String successUrl;

    private final String failUrl;

    public PaymentSaveService(PaymentRepository paymentRepository,
                              @Value("${payments.toss.successUrl}") String successUrl,
                              @Value("${payments.toss.failUrl}") String failUrl) {
        this.paymentRepository = paymentRepository;
        this.successUrl = successUrl;
        this.failUrl = failUrl;
    }

    public Payment save(ReservationPaymentDto dto) {
        Payment payment = new Payment(CARD, dto.getAmount(), dto.getCustomerEmail(), dto.getCustomerName(), dto.getUserId(),
                dto.getReservationID(), successUrl, failUrl, dto.getOrderName());

        return paymentRepository.save(payment);
    }
}
