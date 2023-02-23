package com.example.momobe.reservation.application;

import com.example.momobe.maill.enums.MailType;
import com.example.momobe.reservation.event.ReservationConfirmedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailEventPublishService implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(String userMail, MailType mailType) {
        applicationEventPublisher.publishEvent(new ReservationConfirmedEvent(userMail, mailType));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
