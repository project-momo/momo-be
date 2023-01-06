package com.example.momobe.reservation.domain;

import com.example.momobe.reservation.domain.enums.ReservationState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.momobe.reservation.domain.enums.ReservationState.*;
import static org.assertj.core.api.Assertions.*;

class ReservationTest {

    @Test
    @DisplayName("이미 승인된 상태의 예약을 cancel()할 경우 예외 발생")
    void cancelTest1() {
        // given
        Reservation reservation = Reservation.builder()
                .reservationState(ACCEPT)
                .build();

        // when // then
        assertThatThrownBy(reservation::cancel).isInstanceOf(CanNotChangeReservationStateException.class);
    }

    @Test
    @DisplayName("예약을 cancle()할 경우 예약의 상태가 Canceled로 변경된다")
    void cancelTest2() {
        // given
        Reservation reservation = Reservation.builder()
                .reservationState(PAYMENT_SUCCESS)
                .amount(new Money(0L))
                .build();

        // when
        reservation.cancel();
        ReservationState result = reservation.checkReservationState();

        // then
        assertThat(result).isEqualTo(CANCEL);
    }

    @Test
    @DisplayName("cancel 상태인 예약을 accpet()할 경우 예외 발생")
    void acceptTest1() {
        // given
        Reservation reservation = Reservation.builder()
                .reservationState(CANCEL)
                .build();

        // when // then
        assertThatThrownBy(reservation::accept).isInstanceOf(CanNotChangeReservationStateException.class);
    }

    @Test
    @DisplayName("예약을 accept()할 경우 예약의 상태가 ACCEPT로 변경된다")
    void acceptTest2() {
        // given
        Reservation reservation = Reservation.builder()
                .reservationState(PAYMENT_SUCCESS)
                .build();

        // when
        reservation.accept();
        ReservationState result = reservation.checkReservationState();

        // then
        assertThat(result).isEqualTo(ACCEPT);
    }
}