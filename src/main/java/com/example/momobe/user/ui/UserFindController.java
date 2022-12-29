package com.example.momobe.user.ui;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.domain.MeetingRepository;
import com.example.momobe.user.application.UserCommonService;
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
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final MeetingRepository meetingRepository;

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUser(@Token UserInfo request){
        User findUser = userRepository.findUserByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException(ErrorCode.DATA_NOT_FOUND));
        return mapper.userDtoOfUser(findUser);
    }

//    @GetMapping("/meetings/{meeting-id}/reservations")
//    @ResponseStatus(HttpStatus.OK)
//    public UserResponseDto getUserReservation(@Token UserInfo request, @PathVariable(name = "meeting-id") Long meetingId){
//        meetingRepository.findById(meetingId);
//        return null;
//    }

}
