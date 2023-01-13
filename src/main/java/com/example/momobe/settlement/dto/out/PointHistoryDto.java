package com.example.momobe.settlement.dto.out;

import lombok.Getter;

@Getter
public class PointHistoryDto {
    private Long currentPoint;
    private Long requestPoint;
    private String date;
    private String flag;
    private String state;

    public PointHistoryDto(Long currentPoint, Long requestPoint, String date, String flag, String state) {
        this.currentPoint = currentPoint;
        this.requestPoint = requestPoint;
        this.date = date;
        this.flag = flag;
        this.state = state;
    }
}
