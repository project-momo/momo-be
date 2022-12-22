package com.example.momobe.user.infrastructure;

import com.example.momobe.user.domain.RandomPasswordGenerator;
import org.springframework.stereotype.Component;

import static com.example.momobe.security.enums.SecurityConstants.*;

@Component
public class RandomPasswordGeneratorImpl implements RandomPasswordGenerator {
    @Override
    public String generateTemporaryPassword() {
        return generateRandomKey(TEMPORARY_PASSWORD_LENGTH);
    }
}
