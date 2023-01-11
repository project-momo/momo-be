package com.example.momobe.settlement.application;

import com.example.momobe.user.application.UserFindService;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.domain.UserPoint;
import com.example.momobe.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettlementWithdrawalService {
    private final UserFindService userFindService;
    private final UserRepository userRepository;

    public boolean deductPoint(Long userId, Long amount) {
        User user = userFindService.verifyUser(userId);
        UserPoint userPoint = user.getUserPoint();
        UserPoint newUserPoint = userPoint.minus(amount);
        user.changeUserPoint(newUserPoint);
        userRepository.save(user);
        return true;
    }
}
