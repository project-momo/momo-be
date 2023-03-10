package com.example.momobe.reservation.application;

import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.maill.enums.MailType;
import com.example.momobe.meeting.application.MeetingCommonService;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.reservation.dao.UserMailDao;
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
    ReservationFindService reservationFindService;

    @Mock
    UserMailDao userMailDao;

    @Mock
    MailEventPublishService mailEventPublishService;

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

    @Test
    @DisplayName("????????? meeting??? hostId??? ????????? hostId??? ???????????? ????????? ?????? ??????")
    void confirm_test1() {
        // given
        ReflectionTestUtils.setField(meeting, "hostId", userInfo.getId()+1);
        given(meetingCommonService.getMeeting(any())).willReturn(meeting);

        // when // then
        assertThatThrownBy(() -> reservationConfirmService.confirm(ID1, ID1, userInfo, denyDto))
                .isInstanceOf(ReservationException.class);
    }

    @Test
    @DisplayName("dto??? isAccepted??? false??? ?????? reservation??? state??? deny??? ????????????.")
    void confirm_test3() {
        //given
        given(meetingCommonService.getMeeting(any())).willReturn(meeting);
        given(reservationFindService.getReservation(any())).willReturn(reservation);
        given(userMailDao.findMailOf(any())).willReturn(reservedUser.getEmail().getAddress());

        //when
        reservationConfirmService.confirm(meeting.getId(), reservation.getId(), userInfo, denyDto);

        //then
        assertThat(reservation.getReservationState()).isEqualTo(DENY);
    }

    @Test
    @DisplayName("dto??? isAccepted??? true??? ?????? reservation??? state??? accept??? ????????????.")
    void confirm_test4() {
        //given
        given(meetingCommonService.getMeeting(any())).willReturn(meeting);
        given(reservationFindService.getReservation(any())).willReturn(reservation);
        given(userMailDao.findMailOf(any())).willReturn(reservedUser.getEmail().getAddress());

        //when
        reservationConfirmService.confirm(meeting.getId(), reservation.getId(), userInfo, acceptDto);

        //then
        assertThat(reservation.getReservationState()).isEqualTo(ACCEPT);
    }

    @Test
    @DisplayName("????????? ??????????????? ????????? ?????? applicationEventPublisher??? 1??? ???????????? (accept??? ??????)")
    void event_test1() {
        //given
        given(meetingCommonService.getMeeting(any())).willReturn(meeting);
        given(reservationFindService.getReservation(any())).willReturn(reservation);
        given(userMailDao.findMailOf(any())).willReturn(reservedUser.getEmail().getAddress());

        //when
        reservationConfirmService.confirm(meeting.getId(), reservation.getId(), userInfo, acceptDto);

        //then
        verify(mailEventPublishService, times(1)).publish(anyString(), any(MailType.class));
    }

    @Test
    @DisplayName("????????? ??????????????? ????????? ?????? applicationEventPublisher??? 1??? ???????????? (deny??? ??????)")
    void event_test2() {
        //given
        given(meetingCommonService.getMeeting(any())).willReturn(meeting);
        given(reservationFindService.getReservation(any())).willReturn(reservation);
        given(userMailDao.findMailOf(any())).willReturn(reservedUser.getEmail().getAddress());

        //when
        reservationConfirmService.confirm(meeting.getId(), reservation.getId(), userInfo, denyDto);

        //then
        verify(mailEventPublishService, times(1)).publish(anyString(), any(MailType.class));
    }
}