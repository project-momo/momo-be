package com.example.momobe.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends CustomUserRepository, JpaRepository<User, Long> {
}
