package com.example.momobe.settlement.dto.out;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointWithdrawalResponseDto {
    private WithdrawalDto withdrawal;
    private boolean accountAuth;

    public static PointWithdrawalResponseDto of(WithdrawalDto withdrawal, boolean account){
        return PointWithdrawalResponseDto.builder()
                .withdrawal(withdrawal)
                .accountAuth(account)
                .build();
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WithdrawalDto{
        private boolean withdrawal;
        private Long minusPoint;
        private Long currentPoint;
    }
}
