package com.example.momobe.user.application;

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
public class UserFindService {
    private final UserRepository userRepository;

    /** common logic*/
    public User verifyUser(String email){
        return userRepository.findUserByEmail(email).orElseThrow(()-> new UserNotFoundException(ErrorCode.DATA_NOT_FOUND));
    }

}
