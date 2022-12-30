package com.example.momobe.reservation.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservedUser {
    private Long userId;
}
