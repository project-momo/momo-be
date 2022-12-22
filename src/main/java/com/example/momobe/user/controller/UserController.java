package com.example.momobe.user.controller;

import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.dto.JwtTokenDto;
import com.example.momobe.user.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mypage")
public class UserController {
    private User user;

    @DeleteMapping("/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdrawal(@Token UserInfo request){
        user.userWithdrawal(request.getEmail());
    }
}
