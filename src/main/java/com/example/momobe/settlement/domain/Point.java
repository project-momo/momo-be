package com.example.momobe.settlement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Point {
    private Long currentPoint;
    private Long previousPoint;

    public void changeCurrentPoint(Long paymentPrice){
        this.currentPoint = paymentPrice;
    }
}
