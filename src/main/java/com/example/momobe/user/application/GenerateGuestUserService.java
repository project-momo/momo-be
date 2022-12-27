package com.example.momobe.user.application;

import com.example.momobe.common.domain.RandomKeyGenerator;
import com.example.momobe.user.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateGuestUserService {
    private final UserRepository userRepository;
    private final RandomKeyGenerator randomKeyGenerator;
    private final PasswordEncoder passwordEncoder;

    public User generate() {
        String randomKey = randomKeyGenerator.generateRandomKey(6);
        String nickname = "Guest" + randomKey;
        String password = passwordEncoder.encode(randomKey);

        User user = User.builder()
                .nickname(new Nickname(nickname))
                .email(new Email(nickname + "@guest.com"))
                .password(new Password(password))
                .avatar(null)
                .build();

        return userRepository.save(user);
    }
}
