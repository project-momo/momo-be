package com.example.momobe.reservation.application;

import com.example.momobe.common.enums.TestConstants;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.application.MeetingCommonService;
import com.example.momobe.meeting.domain.Address;
import com.example.momobe.meeting.domain.DateTime;
import com.example.momobe.meeting.domain.DateTimeInfo;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.example.momobe.meeting.domain.enums.Tag;
import com.example.momobe.payment.application.SavePaymentService;
import com.example.momobe.payment.mapper.PaymentMapper;
import com.example.momobe.reservation.domain.*;
import com.example.momobe.reservation.domain.enums.ReservationState;
import com.example.momobe.reservation.dto.in.RequestReservationDto;
import com.example.momobe.reservation.dto.out.PaymentResponseDto;
import com.example.momobe.reservation.mapper.ReservationMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.domain.enums.Category.*;
import static com.example.momobe.meeting.domain.enums.DatePolicy.*;
import static com.example.momobe.meeting.domain.enums.MeetingState.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ReserveServiceTest {
    @InjectMocks
    ReserveService reserveService;

    @Mock
    MeetingCommonService meetingCommonService;

    @Mock
    ReservationMapper reservationMapper;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    CountExistReservationService countExistReservationService;

    @Mock
    SavePaymentService savePaymentService;

    @Mock
    PaymentMapper paymentMapper;

    Meeting meeting;
    RequestReservationDto reservationDto;
    UserInfo userInfo;
    Reservation reservation;

    @BeforeEach
    void init() {
        meeting = Meeting.builder()
                .hostId(ID1)
                .category(AI)
                .title(CONTENT1)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(FREE)
                        .startDate(LocalDate.of(2022,1,1))
                        .endDate(LocalDate.of(2022,1,10))
                        .startTime(LocalTime.of(10,0,0))
                        .endTime(LocalTime.of(18,0,0))
                        .maxTime(4)
                        .dateTimes(List.of(new DateTime(LocalDateTime.of(2022,1,1,12,0,0))))
                        .build())
                .personnel(10)
                .price(10000L)
                .content(CONTENT2)
                .notice(CONTENT3)
                .tags(List.of(Tag.OFFLINE, Tag.MENTORING))
                .meetingState(OPEN)
                .address(new Address(List.of(1L,2L),"화곡동"))
                .build();

        reservationDto = RequestReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(RequestReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.of(2022,1,5))
                        .startTime(LocalTime.of(11,0,0))
                        .endTime(LocalTime.of(12,0,0))
                        .build())
                .build();

        userInfo = UserInfo.builder()
                .id(ID1)
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
                        .date(LocalDate.of(2022,1,5))
                        .startTime(LocalTime.of(11,0,0))
                        .endTime(LocalTime.of(12,0,0))
                        .build())
                .reservationMemo(new ReservationMemo(CONTENT1))
                .build();
    }

    @Test
    @DisplayName("meetingCommonService가 1회 호출된다")
    void reserveTest_verify1() {
        //given
        given(meetingCommonService.findMeetingOrThrowException(any())).willReturn(meeting);
        given(reservationRepository.save(any())).willReturn(reservation);

        //when
        PaymentResponseDto reseult = reserveService.reserve(meeting.getId(), reservationDto, userInfo);

        //then
        verify(meetingCommonService, times(1)).findMeetingOrThrowException(any());
    }

    @Test
    @DisplayName("reservationMapper가 인자별로 각 1회씩 총 2회 호출된다")
    void reserveTest_verify2() {
        //given
        given(meetingCommonService.findMeetingOrThrowException(any())).willReturn(meeting);
        given(reservationRepository.save(any())).willReturn(reservation);

        //when
        PaymentResponseDto reseult = reserveService.reserve(meeting.getId(), reservationDto, userInfo);

        //then
        verify(reservationMapper, times(1)).of(any(Meeting.class), any(), any());
        verify(reservationMapper, times(1)).of(any(UserInfo.class), any(), any());
    }

    @Test
    @DisplayName("countExistReservationService가 1회 호출된다.")
    void reserveTest_verify3() {
        //given
        given(meetingCommonService.findMeetingOrThrowException(any())).willReturn(meeting);
        given(reservationRepository.save(any())).willReturn(reservation);

        //when
        PaymentResponseDto reseult = reserveService.reserve(meeting.getId(), reservationDto, userInfo);

        //then
        verify(countExistReservationService, times(1)).countOf(any(), any(), any(), any());
    }

    @Test
    @DisplayName("reservationRepository가 1회 호출된다.")
    void reserveTest_verify4() {
        //given
        given(meetingCommonService.findMeetingOrThrowException(any())).willReturn(meeting);
        given(reservationRepository.save(any())).willReturn(reservation);

        //when
        PaymentResponseDto reseult = reserveService.reserve(meeting.getId(), reservationDto, userInfo);

        //then
        verify(reservationRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("reservationState가 Before인 경우 savePaymentService가 1회 호출된다.")
    void reserveTest_verify5() {
        //given
        given(meetingCommonService.findMeetingOrThrowException(any())).willReturn(meeting);
        given(reservationRepository.save(any())).willReturn(reservation);

        //when
        PaymentResponseDto reseult = reserveService.reserve(meeting.getId(), reservationDto, userInfo);

        //then
        verify(savePaymentService, times(1)).save(any());
    }

    @Test
    @DisplayName("reservationState가 Success인 경우 savePaymentService가 호출되지 않는다.")
    void reserveTest_verify6() {
        //givne
        ReflectionTestUtils.setField(reservation, "reservationState", ReservationState.PAYMENT_SUCCESS);
        given(meetingCommonService.findMeetingOrThrowException(any())).willReturn(meeting);
        given(reservationRepository.save(any())).willReturn(reservation);

        //when
        PaymentResponseDto reseult = reserveService.reserve(meeting.getId(), reservationDto, userInfo);

        //then
        verify(savePaymentService, times(0)).save(any());
    }
}