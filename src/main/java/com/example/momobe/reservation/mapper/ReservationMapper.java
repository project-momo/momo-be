package com.example.momobe.reservation.mapper;

import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.reservation.domain.*;
import com.example.momobe.reservation.dto.in.PostReservationDto;
import com.example.momobe.reservation.dto.out.ReservationPaymentDto;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.time.LocalTime;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    default Reservation of(Meeting meeting, PostReservationDto reservationDto, UserInfo userInfo) {
        LocalDate reservationDate = reservationDto.getDateInfo()
                .getReservationDate();
        LocalTime startTime = reservationDto.getDateInfo()
                .getStartTime();
        LocalTime endTime = reservationDto.getDateInfo()
                .getEndTime();

        return new Reservation(
                new ReservationDate(reservationDate, startTime, endTime),
                new Money(reservationDto.getAmount()),
                new ReservedUser(userInfo.getId()),
                new ReservationMemo(reservationDto.getReservationMemo()),
                meeting.getId()
        );
    }

    default ReservationPaymentDto of(UserInfo userInfo, Reservation reservation, Meeting meeting) {
        return ReservationPaymentDto.builder()
                .amount(reservation.getAmount().getWon())
                .customerEmail(userInfo.getEmail())
                .customerName(userInfo.getNickname())
                .reservationID(reservation.getId())
                .userId(userInfo.getId())
                .orderName(meeting.getTitle())
                .build();
    }
}
