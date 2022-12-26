package com.example.momobe.user.controller;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.user.application.UserCommonService;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.domain.UserNotFoundException;
import com.example.momobe.user.domain.UserRepository;
import com.example.momobe.user.dto.JwtTokenDto;
import com.example.momobe.user.dto.UserDto;
import com.example.momobe.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class UserController {
    private final UserCommonService userCommonService;
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @DeleteMapping("/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean withdrawal(@Token UserInfo request){
        userCommonService.withdrawalUser(request.getEmail());
        return true;
    }

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@Token UserInfo request){
        User findUser = userCommonService.getUser(request.getEmail());
        return mapper.userDtoOfUser(findUser);
    }

//    @GetMapping("/profile/{userId}")
//    @ResponseStatus(HttpStatus.OK)
//    public UserDto getUser(@PathVariable("userId") Long userId){
//        User findUser = userCommonService.getUser(userId);
//        return mapper.userDtoOfUser(findUser);
//
//    }
}
