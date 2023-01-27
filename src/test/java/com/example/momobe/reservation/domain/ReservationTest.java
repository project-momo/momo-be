package com.example.momobe.reservation.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.reservation.domain.enums.ReservationState.*;
import static org.assertj.core.api.Assertions.*;

class ReservationTest {
    @Test
    @DisplayName("예약을 cancle()할 경우 예약의 상태가 Canceled로 변경된다")
    void cancelTest2() {
        // given
        Reservation reservation = Reservation.builder()
                .reservationState(PAYMENT_SUCCESS)
                .reservationDate(ReservationDate.builder()
                        .date(LocalDate.now().plus(1, ChronoUnit.MONTHS))
                        .startTime(LocalTime.of(10, 0))
                        .endTime(LocalTime.of(22, 0))
                        .build())
                .build();

        // when
        reservation.cancel();

        // then
        assertThat(reservation.getReservationState()).isEqualTo(CANCEL);
    }

    @Test
    @DisplayName("cancel 상태인 예약을 accpet()할 경우 예외 발생")
    void acceptTest1() {
        // given
        Reservation reservation = Reservation.builder()
                .reservationState(CANCEL)
                .build();

        // when // then
        assertThatThrownBy(reservation::accept).isInstanceOf(ReservationException.class);
    }

    @Test
    @DisplayName("예약을 accept()할 경우 예약의 상태가 ACCEPT로 변경된다")
    void acceptTest2() {
        // given
        Reservation reservation = Reservation.builder()
                .reservationState(PAYMENT_SUCCESS)
                .reservationDate(ReservationDate.builder()
                        .date(LocalDate.now().plus(1, ChronoUnit.MONTHS))
                        .startTime(LocalTime.of(10, 0))
                        .endTime(LocalTime.of(22, 0))
                        .build())
                .build();

        // when
        reservation.accept();

        // then
        assertThat(reservation.getReservationState()).isEqualTo(ACCEPT);
    }

    @Test
    @DisplayName("payemntId가 해당 인자와 같은 값으로 변경된다.")
    void setPaymentId1() {
        //given
        Reservation reservation = Reservation.builder().build();

        //when
        reservation.setPaymentId(ID1);

        //then
        assertThat(reservation.getPaymentId()).isEqualTo(ID1);
    }

    @Test
    @DisplayName("reservation의 state가 cancel이라면 true 반환")
    void isCanceledReservation1() {
        //given
        Reservation reservation = Reservation.builder()
                .reservationState(CANCEL)
                .build();

        //when
        Boolean result = reservation.isCanceledReservation();

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("reservation state가 cancel이 아니라면 False 반환")
    void isCanceledReservation2() {
        //given
        Reservation reservation1 = Reservation.builder()
                .reservationState(ACCEPT)
                .build();
        Reservation reservation2 = Reservation.builder()
                .reservationState(PAYMENT_SUCCESS)
                .build();
        Reservation reservation3 = Reservation.builder()
                .reservationState(PAYMENT_BEFORE)
                .build();
        Reservation reservation4 = Reservation.builder()
                .reservationState(DENY)
                .build();

        //when
        Boolean result1 = reservation1.isCanceledReservation();
        Boolean result2 = reservation2.isCanceledReservation();
        Boolean result3 = reservation3.isCanceledReservation();
        Boolean result4 = reservation4.isCanceledReservation();

        //then
        Assertions.assertThat(result1).isFalse();
        Assertions.assertThat(result2).isFalse();
        Assertions.assertThat(result3).isFalse();
        Assertions.assertThat(result4).isFalse();
    }

    @DisplayName("reservation Success가 아니라면 false 반환")
    void isPaymentSucceed1() {
        //given
        Reservation reservation1 = Reservation.builder()
                .reservationState(ACCEPT)
                .build();
        Reservation reservation2 = Reservation.builder()
                .reservationState(CANCEL)
                .build();
        Reservation reservation3 = Reservation.builder()
                .reservationState(PAYMENT_BEFORE)
                .build();
        Reservation reservation4 = Reservation.builder()
                .reservationState(DENY)
                .build();

        //when
        Boolean result1 = reservation1.isPaymentSucceed();
        Boolean result2 = reservation2.isPaymentSucceed();
        Boolean result3 = reservation3.isPaymentSucceed();
        Boolean result4 = reservation4.isPaymentSucceed();

        //then
        Assertions.assertThat(result1).isFalse();
        Assertions.assertThat(result2).isFalse();
        Assertions.assertThat(result3).isFalse();
        Assertions.assertThat(result4).isFalse();
    }

    @Test
    @DisplayName("reservation Success라면 true 반환")
    void isPaymentSucceed2() {
        //given
        Reservation reservation = Reservation.builder()
                .reservationState(PAYMENT_SUCCESS)
                .build();

        //when
        Boolean result = reservation.isPaymentSucceed();

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("reservation date, time이 미래 시점이고 reservation state가 PAYMENT_SUCCESS가 아니라면 예외 발생")
    void denyTest1() {
        //given
        Reservation reservation = Reservation.builder()
                .reservationState(ACCEPT)
                .reservationDate(ReservationDate.builder()
                        .date(LocalDate.now().plus(1, ChronoUnit.MONTHS))
                        .startTime(LocalTime.of(10, 0))
                        .endTime(LocalTime.of(22, 0))
                        .build())
                .build();

        //when // then
        org.junit.jupiter.api.Assertions.assertThrows(ReservationException.class, reservation::deny);
    }

    @Test
    @DisplayName("reservation date, time이 미래 시점이고 reservation state가 PAYMENT_SUCCESS가 아니라면 예외 발생")
    void denyTest2() {
        //given
        Reservation reservation = Reservation.builder()
                .reservationState(CANCEL)
                .reservationDate(ReservationDate.builder()
                        .date(LocalDate.now().minus(1, ChronoUnit.MONTHS))
                        .startTime(LocalTime.of(10, 0))
                        .endTime(LocalTime.of(22, 0))
                        .build())
                .build();

        //when then
        org.junit.jupiter.api.Assertions.assertThrows(ReservationException.class, reservation::deny);
    }

    @Test
    @DisplayName("reservation date, time이 미래 시점이고 reservation state가 PAYMENT_SUCCESS라면 State가 deny로 변경된다.")
    void checkAvailabilityOfCancel3() {
        //given
        Reservation reservation = Reservation.builder()
                .reservationState(PAYMENT_SUCCESS)
                .reservationDate(ReservationDate.builder()
                        .date(LocalDate.now().plus(1, ChronoUnit.MONTHS))
                        .startTime(LocalTime.of(10, 0))
                        .endTime(LocalTime.of(22, 0))
                        .build())
                .build();

        //when
        reservation.deny();

        // then
        assertThat(reservation.getReservationState()).isEqualTo(DENY);
    }

    @Test
    @DisplayName("인자로 제공된 userId가 일치하지 않으면 false 반환")
    void matchUserId() {
        //given
        Reservation reservation = Reservation.builder()
                .reservedUser(new ReservedUser(ID1))
                .build();

        //when
        Boolean result = reservation.matchUserId(ID2);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("인자로 제공된 userId가 일치하면 true 반환")
    void matchUserId2() {
        //given
        Reservation reservation = Reservation.builder()
                .reservedUser(new ReservedUser(ID1))
                .build();

        //when
        Boolean result = reservation.matchUserId(ID1);

        //then
        assertThat(result).isTrue();
    }
}