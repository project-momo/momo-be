package com.example.momobe.payment.application;

import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.domain.PaymentRepository;
import com.example.momobe.reservation.dto.out.ReservationPaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.example.momobe.payment.domain.enums.PayType.*;

@Service
@RequiredArgsConstructor
public class SavePaymentService {
    private final PaymentRepository paymentRepository;

    /*
    원인은 모르겠으나 값 적용되지 않아 당장 예약 기능 완성이 우선이라 임시 주석처리 했습니다.
    혹시 다른 분이 해결하게 된다면 "success임시", "fail임시"를 아래 변수들로 대체하고 주석을 삭제해주세요.
    Author : yang_eun_chan
    DateTime : 2022.01.01 14:02
    */
//    @Value("${payments.toss.successUrl}")
//    private final String successUrl;
//
//    @Value("${payments.toss.failUrl}")
//    private final String failUrl;

//    @Value("${payments.toss.secretKey}")
//    private final String secretKey;
//
//    @Value("${payments.toss.clientKey}")
//    private final String clientKey;

    public Payment save(ReservationPaymentDto dto) {
        Payment payment = new Payment(CARD, dto.getAmount(), dto.getCustomerEmail(), dto.getCustomerName(), dto.getUserId(),
                dto.getReservationID(), "success임시", "fail임시", dto.getOrderName());

        return paymentRepository.save(payment);
    }
}
