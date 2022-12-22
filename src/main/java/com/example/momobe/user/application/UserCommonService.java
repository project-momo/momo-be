package com.example.momobe.user.application;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.domain.UserNotFoundException;
import com.example.momobe.user.domain.UserRepository;
import com.example.momobe.user.domain.enums.UserStateType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserCommonService {
    private UserRepository userRepository;

    public User verifyUser(String email){
        return userRepository.findUserByEmail(email).orElseThrow(()-> new UserNotFoundException(ErrorCode.DATA_NOT_FOUND));
    }
//    public User getUser(String email){
//        return verifyUser(email);
//    }

    //회원 탈퇴 처리
    public boolean withdrawalUser(String email){
        User user = verifyUser(email);
        user.setUserState(UserStateType.DEACTIVATED, LocalDateTime.now());
        return true;
    }
}
