package com.example.momobe.settlement.ui;

import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.settlement.application.SettlementWithdrawalService;
import com.example.momobe.settlement.dto.in.PointWithdrawalDto;
import com.example.momobe.settlement.dto.out.PointWithdrawalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SettlementWithdrawalController {
    private final SettlementWithdrawalService withdrawalService;

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/settlement/point/withdrawal")
    public PointWithdrawalResponseDto withdrawalPoint(@Token UserInfo userInfo, @Valid @RequestBody PointWithdrawalDto request){
        boolean withdrawal = withdrawalService.deductPoint(userInfo.getId(),request.getAmount());
        boolean verifyBankAccount = true;
        return PointWithdrawalResponseDto.of(withdrawal,verifyBankAccount);
    }

}
