package com.example.momobe.payment.dao;

import com.example.momobe.payment.domain.Payment;
import com.example.momobe.reservation.domain.Reservation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentReadDaoTest {
    @Autowired
    EntityManager entityManager;

    @Autowired
    PaymentReadDao paymentReadDao;

    @Test
    @DisplayName("orderId로 조회한 payment의 reservationId에 해당하는 reservation을 조회한다")
    void getReservationIdByOrderId_success() {
        //given
        Reservation reservation = Reservation.builder().build();
        entityManager.persist(reservation);

        String orderId = "12345-67891-23456";
        Payment payment = Payment.builder()
                .reservationId(reservation.getId())
                .orderId(orderId)
                .build();
        entityManager.persist(payment);

        //when
        Long result = paymentReadDao.getReservationIdByOrderId(orderId);

        //then
        Assertions.assertThat(result).isEqualTo(reservation.getId());
    }
}