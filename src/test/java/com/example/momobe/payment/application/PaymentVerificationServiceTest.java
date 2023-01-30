package com.example.momobe.payment.application;

import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.domain.PaymentException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentVerificationServiceTest {
    @Mock
    Payment payment;

    @InjectMocks
    PaymentVerificationService paymentVerificationService;

    @Test
    @DisplayName("payment.matchAmount()결과가 false일 경우 예외 발생")
    void verify() {
        //given
        BDDMockito.given(payment.matchAmount(1000L)).willReturn(Boolean.FALSE);

        //when then
        Assertions.assertThatThrownBy(() -> paymentVerificationService.verify(1000L, payment))
                .isInstanceOf(PaymentException.class);
    }

    @Test
    @DisplayName("payment.matchAmount()결과가 true일 경우 true 반환")
    void verify2() {
        //given
        BDDMockito.given(payment.matchAmount(1000L)).willReturn(Boolean.TRUE);

        //when
        Boolean result = paymentVerificationService.verify(1000L, payment);

        //then
        Assertions.assertThat(result).isTrue();
    }
}