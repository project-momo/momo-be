package com.example.momobe.user.domain;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.settlement.domain.PointHistory;
import com.example.momobe.settlement.domain.enums.PointState;
import com.example.momobe.settlement.domain.enums.PointUsedType;
import com.example.momobe.user.domain.enums.UserStateType;
import com.example.momobe.common.domain.BaseTime;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private UserPoint userPoint = new UserPoint(0L);

    @ElementCollection
    @CollectionTable(name = "point_history",joinColumns = @JoinColumn (name = "user_id",referencedColumnName = "user_id",nullable = false))
    private List<PointHistory> histories = new ArrayList<>();

    @OneToOne(cascade = PERSIST)
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    public User(String email, String nickname, String password, Avatar avatar) {
        this.role = new Role(List.of(ROLE_USER));
        this.email = new Email(email);
        this.nickname = new Nickname(nickname);
        this.password = new Password(password);
        this.userPoint = new UserPoint(0L);
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


    public void changeUserPoint(UserPoint point){
        this.userPoint = point;
    }

    public UserPoint minusUserPoint(Long amount,PointUsedType usedType){
        this.histories.add(new PointHistory(this.userPoint.getPoint(),amount, PointState.DEDUCT, usedType));
        return this.userPoint = userPoint.minus(amount);
    }

    public UserPoint plusUserPoint(Long amount,PointUsedType usedType){
        this.histories.add(new PointHistory(this.userPoint.getPoint(),amount, PointState.SAVE, usedType));
        return this.userPoint = userPoint.plus(amount);
    }
}
