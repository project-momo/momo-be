package com.example.momobe.settlement.application;

import com.example.momobe.settlement.domain.PointHistory;
import com.example.momobe.settlement.domain.enums.PointState;
import com.example.momobe.settlement.domain.enums.PointUsedType;
import com.example.momobe.user.application.UserFindService;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SettlementWithdrawalService {
    private final UserFindService userFindService;
    private final UserRepository userRepository;

    public boolean deductPoint(Long userId, Long amount) {
        User user = userFindService.verifyUser(userId);
        user.changeUserPoint(user.minusUserPoint(amount,PointUsedType.WITHDRAWAL));
        userRepository.save(user);
        return true;
    }
}
