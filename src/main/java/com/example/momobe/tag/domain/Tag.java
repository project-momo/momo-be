package com.example.momobe.tag.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String korName;

    @Column(nullable = false)
    private String engName;

    @Builder
    public Tag(String korName, String engName) {
        this.korName = korName;
        this.engName = engName;
    }
}
