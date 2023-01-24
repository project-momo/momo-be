package com.example.momobe.reservation.application;

import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.reservation.domain.CanNotChangeReservationStateException;
import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.reservation.dto.in.DeleteReservationDto;
import com.example.momobe.reservation.event.ReservationCanceledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.momobe.common.exception.enums.ErrorCode.REQUEST_DENIED;

@Service
@RequiredArgsConstructor
public class ReservationCancelService implements ApplicationEventPublisherAware {
    private final ReservationFindService reservationFindService;
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void cancelReservation(Long reservationId, DeleteReservationDto deleteReservationDto, UserInfo userInfo) {
        Reservation reservation = reservationFindService.getReservation(reservationId);

        checkAvailableOfCancel(userInfo, reservation);
        reservation.cancel();

        publishCancelEvent(deleteReservationDto, reservation);
    }

    private void publishCancelEvent(DeleteReservationDto deleteReservationDto, Reservation reservation) {
        ReservationCanceledEvent paymentCancelEvent = reservation.createCancelEvent(deleteReservationDto.getPaymentKey(), deleteReservationDto.getCancelReason());
        applicationEventPublisher.publishEvent(paymentCancelEvent);
    }

    private void checkAvailableOfCancel(UserInfo userInfo, Reservation reservation) {
        if (!reservation.matchUserId(userInfo.getId())) throw new CanNotChangeReservationStateException(REQUEST_DENIED);
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
