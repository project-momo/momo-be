package com.example.momobe.reservation.enums;

import com.example.momobe.reservation.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.momobe.common.enums.TestConstants.CONTENT1;
import static com.example.momobe.reservation.domain.enums.ReservationState.*;

public class ReservationConstants {
    private static final LocalTime startTime = LocalTime.of(1, 0);
    private static final LocalTime endTime = LocalTime.of(5, 0);

    public static Reservation generateAcceptReservation(Long userId, Long meetingId) {
        return new Reservation(new ReservationDate(LocalDate.now(), startTime, endTime), new Money(5000L),
                new ReservedUser(userId), new ReservationMemo(CONTENT1), ACCEPT, meetingId);
    }

    public static Reservation generatePaymentSuccessReservation(Long userId, Long meetingId) {
        return new Reservation(new ReservationDate(LocalDate.now(), startTime, endTime), new Money(5000L),
                new ReservedUser(userId), new ReservationMemo(CONTENT1), PAYMENT_SUCCESS, meetingId);
    }

    public static Reservation generateDenyReservation(Long userId, Long meetingId) {
        return new Reservation(new ReservationDate(LocalDate.now(), startTime, endTime), new Money(5000L),
                new ReservedUser(userId), new ReservationMemo(CONTENT1), DENY, meetingId);
    }

}
