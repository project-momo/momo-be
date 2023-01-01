package com.example.momobe.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Avatar {
    @Id
    @Column(name = "avatar_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String remotePath;

    public Avatar(String remotePath) {
        this.remotePath = remotePath;
    }
}
