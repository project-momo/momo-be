package com.example.momobe.payment.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.payment.dao.PaymentReadDao;
import com.example.momobe.payment.dao.PaymentUpdateDao;
import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.domain.PaymentException;
import com.example.momobe.payment.domain.PaymentRepository;
import com.example.momobe.payment.domain.enums.PayState;
import com.example.momobe.reservation.domain.enums.ReservationState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.example.momobe.common.exception.enums.ErrorCode.*;
import static com.example.momobe.reservation.domain.enums.ReservationState.*;
import static org.springframework.transaction.annotation.Propagation.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentSuccessService {
    private final PaymentUpdateDao paymentUpdateDao;
    private final PaymentReadDao paymentReadDao;
    private final PaymentRepository paymentRepository;

    @Transactional(propagation = MANDATORY)
    public void setSuccessState(String orderId) {
        try {
            setPaymentState(orderId);
            setReservationState(orderId);
        } catch (Exception e) {
            log.error("",e);
            throw new PaymentException(UNABLE_TO_PROCESS);
        }
    }

    private void setPaymentState(String orderId) {
        Payment payment = paymentRepository
                .findPaymentByOrderId(orderId)
                .orElseThrow(() -> new PaymentException(UNABLE_TO_PROCESS));
        payment.changePaymentState(PayState.SUCCESS);
    }

    private void setReservationState(String orderId) {
        Long reservationId = paymentReadDao.getReservationIdByOrderId(orderId);
        paymentUpdateDao.setReservationState(PAYMENT_SUCCESS, reservationId);
    }
}
