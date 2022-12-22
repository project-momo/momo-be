package com.example.momobe.user.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.user.domain.enums.UserState;
import com.example.momobe.user.infrastructure.UserRepositoryImpl;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

import java.time.LocalDateTime;
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
    private UserState userState;

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

    private UserRepository userRepository;

    public User(String email, String nickname, String password, PasswordEncoder passwordEncoder, Avatar avatar) {
        this.role = new Role(List.of(ROLE_USER));
        this.userState = UserState.ACTIVE;
        this.email = new Email(email);
        this.nickname = new Nickname(nickname);
        this.password = new Password(password, passwordEncoder);
        this.point = new Point(0L);
        this.avatar = avatar;
    }

    //회원 탈퇴 처리
    public void userWithdrawal(String email){
        User user = userRepository.findUserByEmail(email).orElseThrow(()-> new CustomException(ErrorCode.EXPIRED_EXCEPTION));
        user.userState = UserState.DEACTIVATED;
//        withdrawalTime = LocalDateTime.now();
    }
    //회원 활성화 상태 확인
    public boolean isUserActive(){
        return this.userState == UserState.ACTIVE;
    }
}
