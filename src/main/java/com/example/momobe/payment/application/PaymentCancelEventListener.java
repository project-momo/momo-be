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
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = ReservationCanceledEvent.class)
    public void cancel(ReservationCanceledEvent event) {
        Map<String, Object> map = createMap(event);

        for (int i=0; i<3; i++) {
            try {
                paymentCancelService.process(event.getPaymentKey(), map);
                Payment payment = paymentCommonService.getPayment(event.getPaymentId());
                payment.cancel();
                break;
            } catch (Exception e) {
                log.error("결제 취소 요청 " + i + "번 실패", e);
                sleep(100);
            }
        }
    }

    private Map<String, Object> createMap(ReservationCanceledEvent event) {
        Map<String, Object> map = new HashMap<>();
        map.put("cancelReason", event.getReason());
        return map;
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
