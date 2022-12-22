package com.example.momobe.user.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {
    private Long point;

    public Point(Long point) {
        this.point = point;
    }
}
