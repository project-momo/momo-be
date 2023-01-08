package com.example.momobe.payment.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    Payment payment;

    @BeforeEach
    void init() {
        payment = Payment.builder().amount(1000L).build();
    }

    @Test
    @DisplayName("matchAmount()실행 시 값이 더 크면 false 반환")
    void matchAmount1() {
        //given
        //when
        Boolean result = payment.matchAmount(payment.getAmount() + 1L);

        //then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("matchAmount()실행 시 값이 일치하면 true 반환")
    void matchAmount2() {
        //given
        //when
        Boolean result = payment.matchAmount(payment.getAmount());

        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("matchAmount()실행 시 값이 더 작으면 false 반환")
    void matchAmount3() {
        //given
        //when
        Boolean result = payment.matchAmount(payment.getAmount() - 1L);

        //then
        Assertions.assertThat(result).isFalse();
    }
}