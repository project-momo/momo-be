package com.example.momobe.maill.application;

import com.example.momobe.reservation.event.ReservationConfirmedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.annotation.Propagation.*;
import static org.springframework.transaction.event.TransactionPhase.*;

@Component
@RequiredArgsConstructor
@Transactional(propagation = REQUIRES_NEW)
public class MailEventListener {
    private final MailSendService mailSendService;

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT, classes = ReservationConfirmedEvent.class)
    public void listen(ReservationConfirmedEvent event) {
        mailSendService.sendTo(event.getAddress(), event.getMailType());
    }
}
