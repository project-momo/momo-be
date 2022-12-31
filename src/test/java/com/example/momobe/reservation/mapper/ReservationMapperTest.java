package com.example.momobe.reservation.mapper;

import com.example.momobe.common.enums.TestConstants;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.domain.DateTime;
import com.example.momobe.meeting.domain.DateTimeInfo;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.reservation.dto.in.RequestReservationDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ReservationMapperTest {
    private ReservationMapper reservationMapper;

    @BeforeEach
    void init() {
        reservationMapper = new ReservationMapperImpl();
    }

    @Test
    void of() {
        //given
        Meeting meeting = Meeting.builder()
                .hostId(ID1)
                .personnel(10)
                .price(10000L)
                .dateTimeInfo(DateTimeInfo.builder()
                        .dateTimes(List.of(new DateTime(LocalDateTime.now())))
                        .build())
                .build();

        RequestReservationDto dto = RequestReservationDto.builder()
                .amount(10000L)
                .reservationMemo(CONTENT1)
                .dateInfo(RequestReservationDto.ReservationDateDto.builder()
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
}