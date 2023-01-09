package com.example.momobe.payment.application;

import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.domain.PaymentRepository;
import com.example.momobe.payment.domain.UnableProceedPaymentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentCommonServiceTest {
    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    PaymentCommonService paymentCommonService;

    @Test
    @DisplayName("결제 정보가 Optional.empty()라면 예외 발생")
    void saveFailTest() {
        //given
        given(paymentRepository.findPaymentByOrderId(ID1.toString())).willReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> paymentCommonService.getPaymentOrThrowException(ID1.toString()))
                .isInstanceOf(UnableProceedPaymentException.class);
    }

    @Test
    @DisplayName("결제 정보가 존재한다면 예외가 발생하지 않는다")
    void saveFailSuccess() {
        //given
        Payment payment = Payment.builder().build();
        given(paymentRepository.findPaymentByOrderId(ID1.toString())).willReturn(Optional.of(payment));

        //when
        Payment result = paymentCommonService.getPaymentOrThrowException(ID1.toString());

        //then
        assertThat(payment).isEqualTo(result);
    }
}