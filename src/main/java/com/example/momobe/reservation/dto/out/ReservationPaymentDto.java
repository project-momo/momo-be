package com.example.momobe.reservation.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ReservationPaymentDto {
    private Long amount;
    private String customerEmail;
    private String customerName;
    private Long userId;
    private Long reservationID;
    private String orderName;
}
