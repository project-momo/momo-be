package com.example.momobe.reservation.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationMemo {
    private String content;
}
