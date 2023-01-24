package com.example.momobe.reservation.application;

import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.application.MeetingCommonService;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.reservation.dao.UserMailQueryRepository;
import com.example.momobe.reservation.domain.*;
import com.example.momobe.reservation.dto.in.PatchReservationDto;
import com.example.momobe.reservation.event.ReservationConfirmedEvent;
import com.example.momobe.user.domain.Email;
import com.example.momobe.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.reservation.domain.enums.ReservationState.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationConfirmServiceTest {
    @InjectMocks
    ReservationConfirmService reservationConfirmService;

    @Mock
    MeetingCommonService meetingCommonService;

    @Mock
    ReservationCommonService reservationCommonService;

    @Mock
    UserMailQueryRepository userMailQueryRepository;

    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    private Meeting meeting;

    private UserInfo userInfo;

    private PatchReservationDto acceptDto;

    private PatchReservationDto denyDto;

    private Reservation reservation;

    private User reservedUser;

    @BeforeEach
    void init() {
        meeting = Meeting.builder()
                .hostId(ID1)
                .build();

        userInfo = UserInfo.builder()
                .id(ID1)
                .email(EMAIL1)
                .nickname(NICKNAME1)
                .roles(ROLE_USER_LIST)
                .build();

        acceptDto = PatchReservationDto
                .builder()
                .isAccepted("TRUE")
                .message("message")
                .build();

        denyDto = PatchReservationDto
                .builder()
                .isAccepted("False")
                .message("message")
                .build();

        reservedUser = User.builder()
                .email(new Email(EMAIL2))
                .build();

        reservation = Reservation.builder()
                .reservationDate(ReservationDate.builder()
                        .date(LocalDate.now().plus(1, ChronoUnit.YEARS))
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now().plus(1, ChronoUnit.HOURS))
                        .build())
                .amount(new Money(10000L))
                .reservationState(PAYMENT_SUCCESS)
                .reservedUser(new ReservedUser(reservedUser.getId()))
                .build();
    }

    @BeforeEach
    void initApplicationEventListener() {
        reservationConfirmService.setApplicationEventPublisher(applicationEventPublisher);
    }

    @Test
    @DisplayName("불러온 meeting의 hostId와 인자의 hostId가 일치하지 않으면 예외 발생")
    void confirm_test1() {
        // given
        ReflectionTestUtils.setField(meeting, "hostId", userInfo.getId()+1);
        given(meetingCommonService.getMeeting(any())).willReturn(meeting);

        // when // then
        assertThatThrownBy(() -> reservationConfirmService.confirm(ID1, ID1, userInfo, denyDto))
                .isInstanceOf(CanNotChangeReservationStateException.class);
    }

    @Test
    @DisplayName("dto의 isAccepted가 false일 경우 reservation의 state가 deny로 변경된다.")
    void confirm_test3() {
        //given
        given(meetingCommonService.getMeeting(any())).willReturn(meeting);
        given(reservationCommonService.getReservation(any())).willReturn(reservation);
        given(userMailQueryRepository.findMailOf(any())).willReturn(reservedUser.getEmail().getAddress());

        //when
        reservationConfirmService.confirm(meeting.getId(), reservation.getId(), userInfo, denyDto);

        //then
        assertThat(reservation.getReservationState()).isEqualTo(DENY);
    }

    @Test
    @DisplayName("dto의 isAccepted가 true일 경우 reservation의 state가 accept로 변경된다.")
    void confirm_test4() {
        //given
        given(meetingCommonService.getMeeting(any())).willReturn(meeting);
        given(reservationCommonService.getReservation(any())).willReturn(reservation);
        given(userMailQueryRepository.findMailOf(any())).willReturn(reservedUser.getEmail().getAddress());

        //when
        reservationConfirmService.confirm(meeting.getId(), reservation.getId(), userInfo, acceptDto);

        //then
        assertThat(reservation.getReservationState()).isEqualTo(ACCEPT);
    }

    @Test
    @DisplayName("로직이 성공적으로 실행될 경우 applicationEventPublisher가 1회 호출된다 (accept의 경우)")
    void event_test1() {
        //given
        given(meetingCommonService.getMeeting(any())).willReturn(meeting);
        given(reservationCommonService.getReservation(any())).willReturn(reservation);
        given(userMailQueryRepository.findMailOf(any())).willReturn(reservedUser.getEmail().getAddress());

        //when
        reservationConfirmService.confirm(meeting.getId(), reservation.getId(), userInfo, acceptDto);

        //then
        verify(applicationEventPublisher, times(1)).publishEvent(any(ReservationConfirmedEvent.class));
    }

    @Test
    @DisplayName("로직이 성공적으로 실행될 경우 applicationEventPublisher가 1회 호출된다 (deny의 경우)")
    void event_test2() {
        //given
        given(meetingCommonService.getMeeting(any())).willReturn(meeting);
        given(reservationCommonService.getReservation(any())).willReturn(reservation);
        given(userMailQueryRepository.findMailOf(any())).willReturn(reservedUser.getEmail().getAddress());

        //when
        reservationConfirmService.confirm(meeting.getId(), reservation.getId(), userInfo, denyDto);

        //then
        verify(applicationEventPublisher, times(1)).publishEvent(any(ReservationConfirmedEvent.class));
    }
}