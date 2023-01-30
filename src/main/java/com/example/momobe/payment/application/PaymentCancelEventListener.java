package com.example.momobe.payment.application;

import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.domain.PaymentException;
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

import static com.example.momobe.common.exception.enums.ErrorCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class PaymentCancelEventListener {
    private final PaymentCancelService paymentCancelService;
    private final PaymentCommonService paymentCommonService;
    private final Integer RETRY_COUNT = 3;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = ReservationCanceledEvent.class)
    public void cancel(ReservationCanceledEvent event) {
        Map<String, Object> map = createMap(event);

        for (int count=0; count < RETRY_COUNT; count++) {
            try {
                paymentCancelService.process(event.getPaymentKey(), map);
                Payment payment = paymentCommonService.getPayment(event.getPaymentId());
                payment.cancel();
                break;
            } catch (Exception e) {
                log.error("결제 취소 요청 " + count + "번 실패", e);
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
            throw new PaymentException(UNABLE_TO_PROCESS);
        }
    }
}
