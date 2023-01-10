package com.example.momobe.reservation.domain;

import lombok.*;

import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservedUser {
    private Long userId;

    protected Boolean isEqualTo(Long userId) {
        return Objects.equals(userId, this.userId);
    }
}
