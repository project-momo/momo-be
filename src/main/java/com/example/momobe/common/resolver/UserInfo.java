package com.example.momobe.common.resolver;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public final class UserInfo {
    @Getter
    private final Long id;
    @Getter
    private final String email;
    private final List<String> roles;

    public UserInfo(Long id, String email, List<String> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

    public List<String> getRoles() {
        return new ArrayList<>(this.roles);
    }
}
