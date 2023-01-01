package com.example.momobe.reservation.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

import java.util.Objects;

import static lombok.AccessLevel.*;

@Embeddable
@AllArgsConstructor
@Access(AccessType.FIELD)
@NoArgsConstructor(access = PROTECTED)
public class Money {
    private Long won;

    public Long getWon() {
        return this.won;
    };

    protected Boolean match(Long won) {
        return Objects.equals(this.won, won);
    }
}
