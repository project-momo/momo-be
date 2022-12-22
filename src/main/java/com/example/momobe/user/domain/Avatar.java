package com.example.momobe.user.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.*;

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
