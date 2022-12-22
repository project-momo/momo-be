package com.example.momobe.user.domain;

import com.example.momobe.common.domain.RandomKeyGenerator;

public interface RandomPasswordGenerator extends RandomKeyGenerator {
    String generateTemporaryPassword();
}
