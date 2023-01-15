package com.example.momobe.user.application;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.domain.UserNotFoundException;
import com.example.momobe.user.domain.UserRepository;
import com.example.momobe.user.domain.UserState;
import com.example.momobe.user.domain.enums.UserStateType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommonService {
    private final UserFindService userFindService;

    public boolean withdrawalUser(Long id){
        User user = userFindService.verifyUser(id);
        user.changeUserState(new UserState(UserStateType.DEACTIVATED, LocalDateTime.now()));
        return true;
    }
}
