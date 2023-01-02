package com.example.momobe.common.resolver;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
public final class UserInfo {
    @Getter
    private final Long id;
    @Getter
    private final String email;
    private final List<String> roles;
    @Getter
    private final String nickname;

    public UserInfo(Long id, String email, List<String> roles, String nickname) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.nickname = nickname;
    }

    public List<String> getRoles() {
        return new ArrayList<>(this.roles);
    }
}
