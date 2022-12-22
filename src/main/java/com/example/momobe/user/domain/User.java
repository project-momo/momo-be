package com.example.momobe.user.domain;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

import java.util.List;

import static com.example.momobe.user.domain.enums.RoleName.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Embedded
    @Builder.Default
    private Role role = new Role(List.of(ROLE_USER));

    @Embedded
    private Email email;

    @Embedded
    private Nickname nickname;

    @Embedded
    private Password password;

    @Embedded
    @Builder.Default
    private Point point = new Point(0L);

    @OneToOne(cascade = PERSIST)
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    public User(String email, String nickname, String password, PasswordEncoder passwordEncoder, Avatar avatar) {
        this.role = new Role(List.of(ROLE_USER));
        this.email = new Email(email);
        this.nickname = new Nickname(nickname);
        this.password = new Password(password, passwordEncoder);
        this.point = new Point(0L);
        this.avatar = avatar;
    }
}
