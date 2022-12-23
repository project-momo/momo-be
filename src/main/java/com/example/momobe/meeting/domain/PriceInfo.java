package com.example.momobe.meeting.domain;

import com.example.momobe.meeting.domain.enums.PricePolicy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@Getter
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
