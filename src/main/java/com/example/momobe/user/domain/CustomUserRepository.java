package com.example.momobe.user.domain;

import java.util.Optional;

public interface CustomUserRepository {
    Optional<User> findUserByEmail(String email);
}
