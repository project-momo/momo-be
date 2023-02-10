package com.example.momobe.reservation.application;

import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.domain.Address;
import com.example.momobe.meeting.domain.DateTime;
import com.example.momobe.meeting.domain.DateTimeInfo;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.payment.domain.Payment;
import com.example.momobe.reservation.domain.*;
import com.example.momobe.reservation.domain.enums.ReservationState;
import com.example.momobe.reservation.dto.in.PostReservationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.common.enums.TestConstants.ID1;
import static com.example.momobe.meeting.domain.enums.Category.AI;
import static com.example.momobe.meeting.domain.enums.DatePolicy.FREE;
import static com.example.momobe.meeting.domain.enums.MeetingState.OPEN;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservationValidateServiceTest {
    @InjectMocks
    ReservationValidateService reservationValidateService;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    GetReservationsAtSameTimeService getReservationsAtSameTimeService;

    Meeting meeting;
    PostReservationDto reservationDto;
    UserInfo userInfo;
    Reservation reservation;
    Payment payment;

    @BeforeEach
    void init() {
        meeting = Meeting.builder()
                .hostId(ID1)
                .category(AI)
                .title(CONTENT1)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(FREE)
                        .startDate(LocalDate.of(2022, 1, 1))
                        .endDate(LocalDate.of(2022, 1, 10))
                        .startTime(LocalTime.of(10, 0, 0))
                        .endTime(LocalTime.of(18, 0, 0))
                        .maxTime(4)
                        .dateTimes(List.of(new DateTime(LocalDateTime.of(2022, 1, 1, 12, 0, 0))))
                        .build())
                .personnel(10)
                .price(10000L)
                .content(CONTENT2)
                .tagIds(List.of(ID1, ID2))
                .meetingState(OPEN)
                .address(new Address(List.of(1L, 2L), "화곡동"))
                .build();

        reservationDto = PostReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.of(2022,1,5))
                        .startTime(LocalTime.of(11,0,0))
                        .endTime(LocalTime.of(12,0,0))
                        .build())
                .build();

        userInfo = UserInfo.builder()
                .id(ID2)
                .email(EMAIL1)
                .nickname(NICKNAME1)
                .roles(List.of(ROLE_USER))
                .build();

        reservation = Reservation.builder()
                .meetingId(ID1)
                .reservationState(ReservationState.PAYMENT_BEFORE)
                .amount(new Money(10000L))
                .reservedUser(new ReservedUser(ID1))
                .reservationDate(ReservationDate.builder()
                        .date(LocalDate.of(2022, 1, 5))
                        .startTime(LocalTime.of(11, 0, 0))
                        .endTime(LocalTime.of(12, 0, 0))
                        .build())
                .reservationMemo(new ReservationMemo(CONTENT1))
                .build();

        payment = Payment.builder()
                .id(ID1)
                .build();
    }

    @Test
    @DisplayName("getReservationsAtSameTimeService() 결과 리스트에 reserve()인자의 userInfo와 같은 id가 있을 경우 예외 발생")
    void reserveTest_failed() {
        //given
        Reservation reservation = Reservation.builder()
                .reservedUser(new ReservedUser(userInfo.getId()))
                .reservationState(ReservationState.ACCEPT)
                .build();

        given(getReservationsAtSameTimeService.getReservations(any(), any(), any(), any())).willReturn(List.of(reservation));

        //when then
        assertThatThrownBy(() -> reservationValidateService.validate(reservationDto, meeting, userInfo))
                .isInstanceOf(ReservationException.class);
    }

    @Test
    @DisplayName("meeting.hostId와 userInfo.userId가 같을 경우 Reservation Exception 발생")
    void reserveTest_failed2() {
        //given
        userInfo = UserInfo.builder()
                .id(meeting.getHostId())
                .build();

        Reservation reservation = Reservation.builder()
                .reservedUser(new ReservedUser(userInfo.getId()))
                .reservationState(ReservationState.ACCEPT)
                .build();
        given(getReservationsAtSameTimeService.getReservations(any(), any(), any(), any())).willReturn(List.of(reservation));

        //when then
        assertThatThrownBy(() -> reservationValidateService.validate(reservationDto, meeting, userInfo))
                .isInstanceOf(ReservationException.class);
    }

    @Test
    @DisplayName("countExistReservationService가 1회 호출된다.")
    void reserveTest_verify3() {
        //when
        reservationValidateService.validate(reservationDto, meeting, userInfo);

        //then
        verify(getReservationsAtSameTimeService, times(1)).getReservations(any(), any(), any(), any());
    }
}