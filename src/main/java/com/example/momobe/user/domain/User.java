package com.example.momobe.user.domain;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.user.domain.enums.UserStateType;
import com.example.momobe.common.domain.BaseTime;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.momobe.user.domain.enums.RoleName.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseTime {
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

    public User(String email, String nickname, String password, Avatar avatar) {
        this.role = new Role(List.of(ROLE_USER));
        this.email = new Email(email);
        this.nickname = new Nickname(nickname);
        this.password = new Password(password);
        this.point = new Point(0L);
        this.avatar = avatar;
        this.userState = new UserState(UserStateType.ACTIVE, LocalDateTime.now());
    }

    public void changeUserState(UserState userState) {
        if(userState.isUserActive()){
            throw new CanNotChangeUserStateException(ErrorCode.UNABLE_TO_PROCESS);
        }
        setUserState(userState);
    }

    private void setUserState(UserState userState){
        this.userState = userState;
    }
}
