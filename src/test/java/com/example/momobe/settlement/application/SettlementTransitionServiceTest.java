package com.example.momobe.settlement.application;

import com.example.momobe.meeting.domain.DateTimeInfo;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.reservation.domain.Money;
import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.settlement.dao.SettlementQueryRepository;
import com.example.momobe.settlement.domain.PointHistory;
import com.example.momobe.settlement.domain.Settlement;
import com.example.momobe.settlement.domain.SettlementRepository;
import com.example.momobe.settlement.domain.exception.CanNotSettleException;
import com.example.momobe.settlement.dto.out.SettlementResponseDto;
import com.example.momobe.user.application.UserFindService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class SettlementTransitionServiceTest {
    @InjectMocks
    private SettlementTransitionService settlementTransitionService;
    @Mock
    private SettlementQueryRepository settlementQueryRepository;
    @Mock
    private SettlementRepository settlementRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserFindService userFindService;

    SettlementResponseDto.SettlementDto dto1;
    SettlementResponseDto.SettlementDto dto2;
    Meeting meeting;
    Reservation reservation;
    Long amounts;
    Settlement settlement;
    List<PointHistory> pointHistories = new ArrayList<>();
    User user;

    @BeforeEach
    void init(){
        user = User.builder()
                .userPoint(new UserPoint(5000L))
                .id(1L)
                .histories(pointHistories)
                .build();
        meeting = Meeting.builder()
                .hostId(user.getId())
                .dateTimeInfo(DateTimeInfo.builder().endDate(LocalDate.now().minusDays(3)).build())
                .build();
        reservation = Reservation.builder()
                .meetingId(meeting.getId())
                .paymentId(2L)
                .amount(new Money(3000L))
                .build();
        settlement = Settlement.builder()
                .amount(13000L)
                .host(meeting.getHostId())
                .build();
        amounts = 5000L;
        dto1 = SettlementResponseDto.SettlementDto.builder()
                .paymentId(1L)
                .meetingId(1L)
                .reservationId(1L)
                .amount(3000L)
                .host(1L)
                .build();
        dto2 = SettlementResponseDto.SettlementDto.builder()
                .paymentId(2L)
                .meetingId(2L)
                .reservationId(2L)
                .amount(7000L)
                .host(2L)
                .build();
    }

    @Test
    @DisplayName("정상 동작_각 메서드 1번씩 호출")
    void test01(){
        //given
        given(settlementQueryRepository.findReservationForMeetingClosed()).willReturn(List.of(dto1));
        given(userFindService.verifyUser(anyLong())).willReturn(user);

        //when
        settlementTransitionService.transitionOfPayment();
        //정산 데이터 확인
        //then
        verify(settlementRepository, times(1)).save(any(Settlement.class));
        verify(userFindService, times(1)).verifyUser(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("정상 동작_userPoint 변경 확인")
    void test02(){
        //given
        given(settlementQueryRepository.findReservationForMeetingClosed()).willReturn(List.of(dto1));
        given(userFindService.verifyUser(anyLong())).willReturn(user);
        given(userRepository.save(any())).willReturn(user);

        //when
        settlementTransitionService.transitionOfPayment();

        //then
        assertThat(user.getUserPoint().getPoint()).isEqualTo(8000L);
    }

    @Test
    @DisplayName("정산 대상 없을 경우 예외 출력")
    void test03(){
        //given
        given(settlementQueryRepository.findReservationForMeetingClosed()).willReturn(Collections.emptyList());

        //when,then
        assertThrows(CanNotSettleException.class, () -> settlementTransitionService.transitionOfPayment());
    }

}
