package com.example.momobe.settlement.ui;

import com.example.momobe.common.dto.PageResponseDto;
import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.settlement.domain.PointHistoryFindRepository;
//import com.example.momobe.settlement.domain.PointHistoryQueryRepository;
import com.example.momobe.settlement.domain.PointHistory;
import com.example.momobe.settlement.dto.out.PointHistoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SettlementFindController {
    private final PointHistoryFindRepository pointHistoryFindRepository;

    @GetMapping("/mypage/point/details")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<PointHistoryResponseDto> getAllPoint(@Token UserInfo userInfo, Pageable pageable){
        Page<PointHistory> pointHistory = pointHistoryFindRepository.findAllByUser_UserId(userInfo.getId(),pageable);
        Page<PointHistoryResponseDto> response = PointHistoryResponseDto.of(pointHistory);
        return PageResponseDto.of(response);
    }
}
