package com.example.momobe.settlement.dto.in;

import com.example.momobe.settlement.domain.enums.BankCode;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointWithdrawalDto {
    @NotNull
    private Long amount;
    private BankAccountInfo accountInfo;

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BankAccountInfo{
        @NotBlank
        private String name;
        @NotBlank
        private BankCode bank;
        @NotBlank
        @Max(16)
        private String account;
    }
}
