package com.example.momobe.reservation.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ReservationEvent {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PaymentCancel {
        private final String paymentKey;
        private final String reason;
        private final Long paymentId;
    }
}
