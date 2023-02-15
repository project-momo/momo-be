package com.example.momobe.reservation.application;

import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.reservation.dao.PaymentDao;
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

import java.util.Objects;

import static com.example.momobe.common.exception.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReservationCancelService {
    private final PaymentDao paymentDAO;
    private final ReservationFindService reservationFindService;
    private final ReservationEventPublishService reservationEventPublishService;

    @Transactional
    public void cancelReservation(Long reservationId, DeleteReservationDto deleteReservationDto, UserInfo userInfo) {
        Reservation reservation = reservationFindService.getReservation(reservationId);

        validateCancellation(userInfo, reservation, deleteReservationDto);
        reservation.cancel();

        if (deleteReservationDto.getPaymentKey() != null) {
            reservationEventPublishService
                    .publishCancelEvent(deleteReservationDto.getPaymentKey(), reservation.getPaymentId(), getReason(deleteReservationDto));
        }
    }

    private void validateCancellation(UserInfo userInfo, Reservation reservation, DeleteReservationDto deleteReservationDto) {
        String savedPaymentKey = paymentDAO.findPaymentKeyByReservationId(reservation.getId());

        if (!Objects.equals(deleteReservationDto.getPaymentKey(), savedPaymentKey)) throw new ReservationException(INVALID_PAYMENT_KEY);

        if (!reservation.matchReservedUserId(userInfo.getId())) throw new ReservationException(REQUEST_DENIED);

        if (reservation.isAccepted()) throw new ReservationException(CAN_NOT_CHANGE_RESERVATION_STATE);
    }

    private String getReason(DeleteReservationDto deleteReservationDto) {
        return (deleteReservationDto.getCancelReason() == null) ? "NULL" : deleteReservationDto.getCancelReason();
    }
}
