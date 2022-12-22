package com.example.momobe.user.infrastructure;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.user.domain.CustomUserRepository;
import com.example.momobe.user.domain.User;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.example.momobe.common.exception.enums.ErrorCode.*;
import static com.example.momobe.user.domain.QUser.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements CustomUserRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<User> findUserByEmail(String email) {
        try {
            return Optional.ofNullable(jpaQueryFactory.select(user)
                    .from(user)
                    .where(user.email.address.eq(email))
                    .fetchOne());
        } catch (NonUniqueResultException e) {
            log.error("",e);
            throw new CustomException(REQUEST_CONFLICT);
        }
    }
}
