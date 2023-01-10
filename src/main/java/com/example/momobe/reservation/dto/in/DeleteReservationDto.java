package com.example.momobe.reservation.dto.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@AllArgsConstructor
public class DeleteReservationDto {
    @NotBlank
    private final String paymentKey;
    @NotBlank
    private final String cancelReason;
}
