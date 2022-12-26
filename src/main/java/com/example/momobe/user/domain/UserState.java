package com.example.momobe.user.domain;

import com.example.momobe.user.domain.enums.UserStateType;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.joda.time.base.BaseDateTime;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Embeddable
@RequiredArgsConstructor
public class UserState {
    @Enumerated(EnumType.STRING)
    private UserStateType userStateType;
    private LocalDateTime dateTime;

    public UserState(UserStateType userState, LocalDateTime dateTime) {
        this.userStateType = userState;
        this.dateTime = dateTime;
    }

    //회원 활성화 상태 확인
    public boolean isUserActive(){
        if(userStateType == UserStateType.ACTIVE){
            return true;
        }else return false;
    }
}
