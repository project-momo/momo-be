package com.example.momobe.reservation.application;

import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.reservation.domain.ReservationException;
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

/*
* TODO : 프론트 요청사항 변경으로 인해 서비스 로직 수정 필요
* Author : yang eun chan
* datetime : 23/02/10
* */
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
        if (deleteReservationDto.getPaymentKey() != null) {
            ReservationCanceledEvent paymentCancelEvent = reservation.createCancelEvent(deleteReservationDto.getPaymentKey(), deleteReservationDto.getCancelReason());
            applicationEventPublisher.publishEvent(paymentCancelEvent);
        }
    }

    private void checkAvailableOfCancel(UserInfo userInfo, Reservation reservation) {
        if (!reservation.matchReservedUserId(userInfo.getId())) throw new ReservationException(REQUEST_DENIED);
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
