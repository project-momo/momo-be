package com.example.momobe.payment.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.domain.UnableProceedPaymentException;
import com.example.momobe.reservation.event.ReservationCanceledEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class PaymentCancelEventListener {
    private final PaymentCancelService paymentCancelService;
    private final PaymentCommonService paymentCommonService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = ReservationCanceledEvent.PaymentCancel.class)
    public void cancel(ReservationCanceledEvent.PaymentCancel event) {
        Map<String, Object> map = createMap(event);

        try {
            paymentCancelService.process(event.getPaymentKey(), map);
            Payment payment = paymentCommonService.getPaymentOrThrowException(event.getPaymentId());
            payment.cancel();
        } catch (UnableProceedPaymentException e) {
            log.error("",e);
        } catch (Exception e) {
            log.error("",e);
            throw new UnableProceedPaymentException(ErrorCode.UNABLE_TO_PROCESS);
        }
    }

    private Map<String, Object> createMap(ReservationCanceledEvent.PaymentCancel event) {
        Map<String, Object> map = new HashMap<>();
        map.put("cancelReason", event.getReason());
        return map;
    }
}
