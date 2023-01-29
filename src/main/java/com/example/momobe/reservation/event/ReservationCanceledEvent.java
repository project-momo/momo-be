package com.example.momobe.reservation.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReservationCanceledEvent {
    private final String paymentKey;
    private final String reason;
    private final Long paymentId;
}
