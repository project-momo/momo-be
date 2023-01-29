package com.example.momobe.payment.application;

import com.example.momobe.common.enums.TestConstants;
import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.domain.enums.PayState;
import com.example.momobe.reservation.event.ReservationCanceledEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentCancelEventListenerTest {
    @InjectMocks
    PaymentCancelEventListener paymentCancelEventListener;

    @Mock
    PaymentCommonService paymentCommonService;

    @Mock
    PaymentCancelService paymentCancelService;

    @Test
    @DisplayName("예외 발생 시 3회까지 재시도하며 paymentCancelService 3회 호출")
    void cancel1() {
        //given
        ReservationCanceledEvent event = ReservationCanceledEvent
                .builder()
                .paymentId(ID1)
                .paymentKey(ID2.toString())
                .build();

        willThrow(new RuntimeException()).given(paymentCancelService).process(any(), any());
        //when
        paymentCancelEventListener.cancel(event);
        //then
        verify(paymentCancelService, Mockito.times(3)).process(any(), any());
    }

    @Test
    @DisplayName("예외가 발생하지 않을 경우 paymentCancelService가 1회 호출된다.")
    void cancel2() {
        //given
        ReservationCanceledEvent event = ReservationCanceledEvent
                .builder()
                .paymentId(ID1)
                .paymentKey(ID2.toString())
                .build();

        Payment request = Payment.builder()
                .payState(PayState.SUCCESS)
                .build();

        willDoNothing().given(paymentCancelService).process(any(), any());
        given(paymentCommonService.getPayment(anyLong())).willReturn(request);
        //when
        paymentCancelEventListener.cancel(event);
        //then
        verify(paymentCancelService, Mockito.times(1)).process(any(), any());
    }

    @Test
    @DisplayName("예외가 발생하지 않을 경우 paymentCommonService.getPayment()가 1회 호출된다.")
    void cancel3() {
        //given
        ReservationCanceledEvent event = ReservationCanceledEvent
                .builder()
                .paymentId(ID1)
                .paymentKey(ID2.toString())
                .build();

        Payment request = Payment.builder()
                .payState(PayState.SUCCESS)
                .build();

        willDoNothing().given(paymentCancelService).process(any(), any());
        given(paymentCommonService.getPayment(anyLong())).willReturn(request);
        //when
        paymentCancelEventListener.cancel(event);
        //then
        verify(paymentCommonService, Mockito.times(1)).getPayment(anyLong());
    }
}