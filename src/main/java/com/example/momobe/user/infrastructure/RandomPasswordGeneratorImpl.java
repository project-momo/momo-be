package com.example.momobe.common.infrastructure;

import com.example.momobe.common.domain.RandomKeyGenerator;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;

import static com.example.momobe.security.enums.SecurityConstants.*;

@Component
public class RandomKeyGeneratorImpl implements RandomKeyGenerator {
    public String generateMailAuthKey() { return generateRandomKey(EMAIL_AUTH_KEY_LENGTH); }
    public String generateTemporaryPassword() {
        return generateRandomKey(TEMPORARY_PASSWORD_LENGTH);
    }
}
