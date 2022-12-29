package com.example.momobe.user.ui;

import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.user.application.UserCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class UserWithdrawalController {
    private final UserCommonService userCommonService;

    @DeleteMapping("/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean withdrawal(@Token UserInfo request){
        userCommonService.withdrawalUser(request.getEmail());
        return true;
    }
}
