package com.example.momobe.maill.application;

import com.example.momobe.reservation.event.ReservationConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.annotation.Propagation.*;
import static org.springframework.transaction.event.TransactionPhase.*;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(propagation = REQUIRES_NEW)
public class MailEventListener {
    private final MailSendService mailSendService;

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT, classes = ReservationConfirmedEvent.class)
    public void listen(ReservationConfirmedEvent event) {
        for (int i=0; i<3; i++) {
            try {
                mailSendService.sendTo(event.getAddress(), event.getMailType());
                break;
            } catch (Exception e) {
                log.error("메일 전송 요청 " + i + "번 실패", e);
                sleep(100);
            }
        }
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
