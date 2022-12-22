package com.example.momobe.meeting.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PriceInfo {
    @Enumerated(STRING)
    @Column(nullable = false)
    private PricePolicy pricePolicy;
    @Column(nullable = false)
    private Long price;

    public PriceInfo(PricePolicy pricePolicy, Long price) {
        this.pricePolicy = pricePolicy;
        this.price = price;
    }
}
