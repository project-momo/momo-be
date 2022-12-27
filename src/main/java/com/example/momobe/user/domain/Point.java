package com.example.momobe.user.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;
import lombok.*;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

import static com.example.momobe.common.exception.enums.ErrorCode.*;

@Embeddable
@Access(AccessType.FIELD)
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {
    private Long point;

    public Point(Long point) {
        this.point = point;
    }

    public Point plus(Long point) {
        return new Point(this.point + point);
    }

    public Point minus(Long point) {
        if (this.point - point < 0) throw new NotEnoughPointException(REQUEST_CONFLICT);
        return new Point(this.point - point);
    }

    public Long getPoint() {
        return this.point;
    }
}
