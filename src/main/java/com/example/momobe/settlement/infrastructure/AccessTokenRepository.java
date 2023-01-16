package com.example.momobe.settlement.infrastructure;

import com.example.momobe.settlement.domain.OpenApiAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<OpenApiAccessToken,Long> {
    Optional<OpenApiAccessToken> findFirstByExpireDateAfter(String now);
}
