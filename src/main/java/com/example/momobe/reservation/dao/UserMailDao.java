package com.example.momobe.reservation.dao;

import com.example.momobe.user.infrastructure.UnableToProcessException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Objects;

import static com.example.momobe.common.exception.enums.ErrorCode.*;
import static com.example.momobe.user.domain.QUser.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserMailDao {
    private final JPAQueryFactory jpaQueryFactory;

    public String findMailOf(Long userId) {
        try {
            return Objects.requireNonNull(jpaQueryFactory.select(user.email)
                            .from(user)
                            .where(user.id.eq(userId))
                            .fetchOne())
                    .getAddress();
        } catch (Exception e) {
            log.error("", e);
            throw new UnableToProcessException(INVALID_EMAIL);
        }
    }
}
