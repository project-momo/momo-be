package com.example.momobe.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {
    @Getter
    private String address;

    public Email(String address) {
        this.address = address;
    }
}
