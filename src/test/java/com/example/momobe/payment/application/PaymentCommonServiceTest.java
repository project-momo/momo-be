package com.example.momobe.payment.application;

import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.domain.PaymentRepository;
import com.example.momobe.payment.domain.PaymentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    void saveFailTgetPaymentOrThrowExceptionest1() {
        //given
        given(paymentRepository.findPaymentByOrderId(ID1.toString())).willReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> paymentCommonService.getPayment(ID1.toString()))
                .isInstanceOf(PaymentException.class);
    }

    @Test
    @DisplayName("결제 정보가 존재한다면 예외가 발생하지 않는다")
    void getPaymentOrThrowException2() {
        //given
        Payment payment = Payment.builder().build();
        given(paymentRepository.findPaymentByOrderId(ID1.toString())).willReturn(Optional.of(payment));

        //when
        Payment result = paymentCommonService.getPayment(ID1.toString());

        //then
        assertThat(payment).isEqualTo(result);
    }

    @Test
    @DisplayName("결제 정보가 존재한다면 예외가 발생하지 않는다 (Long 조회)")
    void getPaymentOrThrowException3() {
        //given
        Payment payment = Payment.builder().build();
        given(paymentRepository.findById(ID1)).willReturn(Optional.of(payment));

        //when
        Payment result = paymentCommonService.getPayment(ID1);

        //then
        assertThat(payment).isEqualTo(result);
    }

    @Test
    @DisplayName("결제 정보가 Optional.empty()라면 예외 발생(Long 조회)")
    void saveFailTgetPaymentOrThrowExceptionest4() {
        //given
        given(paymentRepository.findById(ID1)).willReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> paymentCommonService.getPayment(ID1))
                .isInstanceOf(PaymentException.class);
    }
}