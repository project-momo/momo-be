package com.example.momobe.payment.dao;

import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.reservation.domain.enums.ReservationState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PaymentUpdateDaoTest {
    @Autowired
    PaymentUpdateDao paymentUpdateDao;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("인자로 받은 reservationId를 가진 reservation의 state를 변경한다")
    void setReservationStateTest1() {
        //given
        Reservation reservation = Reservation.builder()
                .reservationState(ReservationState.PAYMENT_BEFORE)
                .build();

        entityManager.persist(reservation);
        entityManager.flush();
        entityManager.clear();

        //when
        paymentUpdateDao.setReservationState(ReservationState.PAYMENT_SUCCESS, reservation.getId());

        //then
        Reservation result = entityManager.find(Reservation.class, reservation.getId());
        assertThat(result.getReservationState()).isEqualTo(ReservationState.PAYMENT_SUCCESS);
    }

    @Test
    @DisplayName("인자로 받은 reservationId를 가진 reservation의 state를 변경한다")
    void setReservationStateTest2() {
        //given
        Reservation reservation = Reservation.builder()
                .reservationState(ReservationState.PAYMENT_BEFORE)
                .build();

        entityManager.persist(reservation);
        entityManager.flush();
        entityManager.clear();

        //when
        paymentUpdateDao.setReservationState(ReservationState.CANCEL, reservation.getId());

        //then
        Reservation result = entityManager.find(Reservation.class, reservation.getId());
        assertThat(result.getReservationState()).isEqualTo(ReservationState.CANCEL);
    }
}