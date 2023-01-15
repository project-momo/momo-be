package com.example.momobe.reservation.dto.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteReservationDto {
    @NotBlank
    private String paymentKey;
    @NotBlank
    private String cancelReason;
}
