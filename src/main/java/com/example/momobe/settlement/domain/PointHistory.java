package com.example.momobe.settlement.domain;

import com.example.momobe.common.domain.BaseTime;
import com.example.momobe.settlement.domain.enums.PointState;
import com.example.momobe.settlement.domain.enums.PointUsedType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointHistory {
    private LocalDate historyDate = LocalDate.now();
    private Long currentPoint;
    private Long requestPoint;

    @Enumerated(EnumType.STRING)
    private PointState state; //적립,출금 구분

    @Enumerated(EnumType.STRING)
    private PointUsedType type; //정산,인출,차감 등 상태 구분

    public PointHistory(Long currentPoint, Long requestPoint, PointState state, PointUsedType type) {
        this.currentPoint = currentPoint;
        this.requestPoint = requestPoint;
        this.state = state;
        this.type = type;
    }
}
