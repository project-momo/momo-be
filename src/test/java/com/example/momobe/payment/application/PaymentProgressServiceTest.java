package com.example.momobe.payment.application;


import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.dto.PaymentResultDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentProgressServiceTest {
    @Mock
    PaymentCommonService paymentCommonService;

    @Mock
    PaymentVerificationService paymentVerificationService;

    @Mock
    PaymentRequestService paymentRequestService;

    @Mock
    PaymentSuccessService paymentSuccessService;

    @InjectMocks
    PaymentProgressService paymentProgressService;

    @Test
    @DisplayName("paymentCommonService가 1회 호출된다")
    void verifyTest1() {
        //given
        //when
        paymentProgressService.progress(ID, ID, 1000L);

        //then
        verify(paymentCommonService, Mockito.times(1)).getPayment(ID);
    }

    @Test
    @DisplayName("paymentVerificationService가 1회 호출된다")
    void verifyTest2() {
        //given
        Payment payment = Payment.builder().build();
        given(paymentCommonService.getPayment(ID)).willReturn(payment);

        //when
        paymentProgressService.progress(ID, ID, 1000L);

        //then
        verify(paymentVerificationService, Mockito.times(1)).verify(1000L, payment);
    }

    @Test
    @DisplayName("함수가 전부 수행되고나면, payment에 인자로 받은 paymentKey 값이 저장된다.")
    void verifyTest4() {
        //given
        Payment payment = Payment.builder().build();
        given(paymentCommonService.getPayment(ID)).willReturn(payment);
        given(paymentVerificationService.verify(1000L, payment)).willReturn(Boolean.TRUE);

        //when
        paymentProgressService.progress(ID, ID, 1000L);

        //then
        assertThat(payment.getPaymentKey()).isEqualTo(ID);
    }
}