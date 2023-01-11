package com.example.momobe.settlement.ui;

import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.settlement.application.SettlementWithdrawalService;
import com.example.momobe.settlement.dto.PointDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SettlementController {
    private final SettlementWithdrawalService withdrawalService;

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/settlement/point/withdrawal")
    public boolean withdrawalPoint(@Token UserInfo userInfo, @Valid @RequestBody PointDto.Withdrawal request){
        return withdrawalService.deductPoint(userInfo.getId(),request.getAmount());
    }

}