package com.example.momobe.reservation.mapper;

import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.domain.DateTime;
import com.example.momobe.meeting.domain.DateTimeInfo;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.reservation.domain.Money;
import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.reservation.dto.in.PostReservationDto;
import com.example.momobe.reservation.dto.out.ReservationPaymentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.assertj.core.api.Assertions.*;

class ReservationMapperTest {
    private ReservationMapper reservationMapper;

    @BeforeEach
    void init() {
        reservationMapper = new ReservationMapperImpl();
    }

    @Test
    void of1() {
        //given
        Meeting meeting = Meeting.builder()
                .hostId(ID1)
                .personnel(10)
                .price(10000L)
                .dateTimeInfo(DateTimeInfo.builder()
                        .dateTimes(List.of(new DateTime(LocalDateTime.now())))
                        .build())
                .build();

        PostReservationDto dto = PostReservationDto.builder()
                .amount(10000L)
                .reservationMemo(CONTENT1)
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.of(2022, 1, 1))
                        .startTime(LocalTime.of(6, 0))
                        .endTime(LocalTime.of(7, 0))
                        .build())
                .build();

        UserInfo user = UserInfo.builder()
                .id(ID1)
                .build();

        //when
        Reservation result = reservationMapper.of(meeting, dto, user);

        //then
        assertThat(result.getAmount().getWon()).isEqualTo(dto.getAmount());
        assertThat(result.getReservationDate().getDate()).isEqualTo(dto.getDateInfo().getReservationDate());
        assertThat(result.getReservationDate().getStartTime()).isEqualTo(dto.getDateInfo().getStartTime());
        assertThat(result.getReservationDate().getEndTime()).isEqualTo(dto.getDateInfo().getEndTime());
        assertThat(result.getReservationMemo().getContent()).isEqualTo(dto.getReservationMemo());
        assertThat(result.getReservedUser().getUserId()).isEqualTo(user.getId());
    }

    @Test
    void of2() {
        //given
        Meeting meeting = Meeting.builder()
                .title(CONTENT1)
                .dateTimeInfo(DateTimeInfo.builder()
                        .dateTimes(List.of(new DateTime(LocalDateTime.now())))
                        .build())
                .build();

        Reservation reservation = Reservation.builder()
                .amount(new Money(10000L))
                .id(ID1)
                .build();

        UserInfo user = UserInfo.builder()
                .id(ID1)
                .nickname(NICKNAME1)
                .email(EMAIL1)
                .build();

        //when
        ReservationPaymentDto result = reservationMapper.of(user, reservation, meeting);

        //then
        assertThat(result.getAmount()).isEqualTo(reservation.getAmount().getWon());
        assertThat(result.getCustomerEmail()).isEqualTo(user.getEmail());
        assertThat(result.getCustomerName()).isEqualTo(user.getNickname());
        assertThat(result.getUserId()).isEqualTo(user.getId());
        assertThat(result.getReservationID()).isEqualTo(reservation.getId());
        assertThat(result.getOrderName()).isEqualTo(meeting.getTitle());
    }
}