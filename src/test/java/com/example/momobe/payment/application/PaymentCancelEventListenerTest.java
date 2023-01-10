package com.example.momobe.payment.application;

import com.example.momobe.payment.domain.UnableProceedPaymentException;
import com.example.momobe.reservation.event.ReservationEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PaymentCancelEventListenerTest {
    @InjectMocks
    PaymentCancelEventListener paymentCancelEventListener;

    @Mock
    PaymentCommonService paymentCommonService;

    @Mock
    PaymentCancelService paymentCancelService;

    @Test
    @DisplayName("예기치 못한 예외가 발생했을 때 UnableToProcess로 throw한다")
    void cancel() {
        //given
        BDDMockito.willThrow(new RuntimeException()).given(paymentCancelService).cancelPayment(any(), any());
        //when
        //then
        Assertions.assertThatThrownBy(() -> paymentCancelEventListener.cancel(ReservationEvent.PaymentCancel.builder().build()))
                .isInstanceOf(UnableProceedPaymentException.class);
    }
}