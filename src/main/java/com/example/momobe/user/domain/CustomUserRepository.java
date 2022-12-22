package com.example.momobe.user.domain;

import com.example.momobe.user.domain.User;

import java.util.Optional;

public interface CustomUserRepository {
    Optional<User> findUserByEmail(String email);
}
