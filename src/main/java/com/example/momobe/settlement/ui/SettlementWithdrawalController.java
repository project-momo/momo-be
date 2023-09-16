package com.example.momobe.settlement.ui;

import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.settlement.application.OpenApiService;
import com.example.momobe.settlement.application.SettlementWithdrawalService;
import com.example.momobe.settlement.dto.in.PointWithdrawalDto;
import com.example.momobe.settlement.dto.out.PointWithdrawalResponseDto;
import com.example.momobe.user.domain.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SettlementWithdrawalController {
    private final SettlementWithdrawalService withdrawalService;
    private final OpenApiService openApiService;

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/mypage/point/withdrawal")
    public PointWithdrawalResponseDto withdrawalPoint(@Token UserInfo userInfo, @Valid @RequestBody PointWithdrawalDto request){
        boolean verifyBankAccount = openApiService.verifyBankAccount(request.getAccountInfo());
        return withdrawalService.deductPoint(userInfo.getId(),request.getAmount(),verifyBankAccount);
    }
}
