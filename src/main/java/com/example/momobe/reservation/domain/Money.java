package com.example.momobe.reservation.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

import static lombok.AccessLevel.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Money {
    private Long won;
}
