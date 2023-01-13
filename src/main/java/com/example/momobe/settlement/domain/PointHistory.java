package com.example.momobe.settlement.domain;

import com.example.momobe.common.domain.BaseTime;
import com.example.momobe.settlement.domain.enums.PointState;
import com.example.momobe.settlement.domain.enums.PointUsedType;
import com.example.momobe.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointHistory extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    private LocalDate historyDate = LocalDate.now();
    private Long currentPoint;
    private Long requestPoint;
    @Enumerated(EnumType.STRING)
    private PointState state;

    @Enumerated(EnumType.STRING)
    private PointUsedType type;

    @Column(name = "user_id")
    private Long user;

    public PointHistory(Long userId, Long currentPoint, Long requestPoint, PointState state, PointUsedType type) {
        this.user = userId;
        this.currentPoint = currentPoint;
        this.requestPoint = requestPoint;
        this.state = state;
        this.type = type;
    }
}
