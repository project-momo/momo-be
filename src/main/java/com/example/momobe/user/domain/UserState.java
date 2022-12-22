package com.example.momobe.user.domain;

import com.example.momobe.user.domain.enums.UserStateType;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;
import org.joda.time.base.BaseDateTime;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
@RequiredArgsConstructor
public class UserState {
    private UserStateType userStateType;
    private LocalDateTime withdrawalTime;

    public UserState(UserStateType userState, java.time.LocalDateTime dateTime) {
    }

    //회원 활성화 상태 확인
    public boolean isUserActive(User user){
        if(userStateType == UserStateType.ACTIVE){
            return true;
        }else return false;
    }
}
