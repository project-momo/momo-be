package com.example.momobe.user.dto;

import com.example.momobe.user.domain.Email;
import com.example.momobe.user.domain.Nickname;
import com.example.momobe.user.domain.Role;
import lombok.*;

@Getter
@Builder
@RequiredArgsConstructor
public class RedisUserDto {
    private final Long id;
    private final Nickname nickname;
    private final Email email;
    private final Role role;
}
