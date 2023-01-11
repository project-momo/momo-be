package com.example.momobe.user.ui;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.domain.MeetingRepository;
import com.example.momobe.user.application.UserCommonService;
import com.example.momobe.user.application.UserFindService;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.domain.UserNotFoundException;
import com.example.momobe.user.domain.UserRepository;
import com.example.momobe.user.dto.UserResponseDto;
import com.example.momobe.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class UserFindController {
    private final UserFindService userFindService;
    private final UserMapper mapper;

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUser(@Token UserInfo request){
        User findUser = userFindService.verifyUser(request.getId());
        return mapper.userDtoOfUser(findUser);
    }
}
