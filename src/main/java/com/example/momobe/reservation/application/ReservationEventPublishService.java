package com.example.momobe.reservation.application;

import com.example.momobe.reservation.event.ReservationCanceledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationEventPublishService implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    protected void publishCancelEvent(String paymentKey, Long paymentId, String reason) {
        ReservationCanceledEvent cancelEvent = ReservationCanceledEvent.builder()
                .paymentKey(paymentKey)
                .paymentId(paymentId)
                .reason(reason)
                .build();

        applicationEventPublisher.publishEvent(cancelEvent);
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
