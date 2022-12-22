package com.example.momobe.user.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

import static javax.persistence.AccessType.*;
import static lombok.AccessLevel.*;

@Embeddable
@Access(FIELD)
@NoArgsConstructor(access = PROTECTED)
public class Password {
    private String password;

    public Password(String password) {
        this.password = password;
    }

    public boolean match(String password) {
        return this.password.equals(password);
    }

    protected String getPassword() {
        return this.password;
    }
}
