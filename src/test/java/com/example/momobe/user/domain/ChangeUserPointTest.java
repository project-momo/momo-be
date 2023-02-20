package com.example.momobe.user.domain;


import com.example.momobe.settlement.application.SettlementWithdrawalService;
import com.example.momobe.settlement.domain.PointHistory;
import com.example.momobe.settlement.domain.PointHistoryFindRepository;
import com.example.momobe.settlement.domain.enums.PointState;
import com.example.momobe.settlement.domain.enums.PointUsedType;
import com.example.momobe.user.application.UserFindService;
import com.example.momobe.user.domain.NotEnoughPointException;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.domain.UserPoint;
import com.example.momobe.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ChangeUserPointTest {

    @InjectMocks
    private SettlementWithdrawalService withdrawalService;

    @Mock
    private UserFindService userFindService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PointHistoryFindRepository pointHistoryFindRepository;


    User user1;
    User user2;
    UserPoint userPoint;
    Long deductPoint = 5000L;
    PointHistory pointHistory1;
    PointHistory pointHistory2;

    @BeforeEach
    void init(){
        user1 = User.builder()
                .id(1L)
                .userPoint(new UserPoint(7000L))
                .histories(new ArrayList<>())
                .build();

        user2 = User.builder()
                .id(2L)
                .userPoint(new UserPoint(3000L))
                .histories(new ArrayList<>())
                .build();

        userPoint = new UserPoint(0L);

        pointHistory1 = PointHistory.builder()
                .id(1L)
                .currentPoint(user1.getUserPoint().getPoint())
                .user(user1.getId())
                .state(PointState.DEDUCT)
                .type(PointUsedType.WITHDRAWAL)
                .build();

        pointHistory2 = PointHistory.builder()
                .id(1L)
                .currentPoint(user2.getUserPoint().getPoint())
                .user(user2.getId())
                .state(PointState.SAVE)
                .type(PointUsedType.SETTLEMENT)
                .build();
    }
    @Test
    @DisplayName("정상 동작_포인트 차감")
    void test01(){
        user1.getHistories().add(pointHistory1);
        userPoint = user1.minusUserPoint(deductPoint,PointUsedType.WITHDRAWAL);

        assertThat(user1.getUserPoint().getPoint()).isEqualTo(userPoint.getPoint());
        assertThat(user1.getHistories().get(0).getType()).isEqualTo(PointUsedType.WITHDRAWAL);
        assertThat(user1.getHistories().get(0).getState()).isEqualTo(PointState.DEDUCT);
    }

    @Test
    @DisplayName("정상 동작_포인트 적립")
    void test02(){
        user2.getHistories().add(pointHistory2);
        userPoint = user2.plusUserPoint(deductPoint,PointUsedType.SETTLEMENT);

        assertThat(user2.getUserPoint().getPoint()).isEqualTo(userPoint.getPoint());
        assertThat(user2.getHistories().get(0).getType()).isEqualTo(PointUsedType.SETTLEMENT);
        assertThat(user2.getHistories().get(0).getState()).isEqualTo(PointState.SAVE);
    }


    @Test
    @DisplayName("잔여 포인트보다 차감 요청 포인트가 많을 경우 409 반환")
    void test03(){
        pointHistory2 = PointHistory.builder()
                .id(1L)
                .currentPoint(user1.getUserPoint().getPoint())
                .user(user1.getId())
                .type(PointUsedType.WITHDRAWAL)
                .build();
        user2.getHistories().add(pointHistory2);

        assertThrows(NotEnoughPointException.class, () -> user2.minusUserPoint(deductPoint,PointUsedType.WITHDRAWAL));
    }
}
