package com.example.momobe.user.domain;

import com.example.momobe.settlement.domain.PointHistory;
import lombok.*;

import javax.persistence.*;

import static com.example.momobe.common.exception.enums.ErrorCode.*;

@Embeddable
@Access(AccessType.FIELD)
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPoint {
    @Column(nullable = false, name = "user_point")
    private Long point;

    public UserPoint(Long point) {
        this.point = point;
    }

    public UserPoint plus(Long point) {

        return new UserPoint(this.point + point);
    }

    public UserPoint minus(Long point) {
        if (this.point - point < 0) throw new NotEnoughPointException(REQUEST_CONFLICT);
        return new UserPoint(this.point - point);
    }

    public Long getPoint() {
        return this.point;
    }
}
