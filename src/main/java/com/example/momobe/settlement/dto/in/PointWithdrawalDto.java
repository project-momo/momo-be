package com.example.momobe.settlement.dto.in;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointWithdrawalDto {
    @NotNull
    private Long amount;
}
