package com.example.momobe.settlement.dto.out;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointWithdrawalResponseDto {
    private boolean withdrawal;
    private boolean account;

    public static PointWithdrawalResponseDto of(boolean withdrawal, boolean account){
        return new PointWithdrawalResponseDto(withdrawal,account);
    }
}
