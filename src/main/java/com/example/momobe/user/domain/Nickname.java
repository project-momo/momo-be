package com.example.momobe.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

import static lombok.AccessLevel.*;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Nickname {
    @Getter
    private String nickname;

    public Nickname(String nickname) {
        this.nickname = nickname;
    }
}
