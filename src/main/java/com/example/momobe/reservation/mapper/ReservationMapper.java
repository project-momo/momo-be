package com.example.momobe.reservation.mapper;

import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.reservation.domain.*;
import com.example.momobe.reservation.dto.in.RequestReservationDto;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.time.LocalTime;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    default Reservation of(Meeting meeting, RequestReservationDto reservationDto, UserInfo userInfo) {
        LocalDate reservationDate = reservationDto.getDateInfo()
                .getReservationDate();
        LocalTime startTime = reservationDto.getDateInfo()
                .getStartTime();
        LocalTime endTime = reservationDto.getDateInfo()
                .getEndTime();

        return new Reservation(
                new ReservationDate(reservationDate, startTime, endTime),
                new Money(meeting.getPrice()),
                new ReservedUser(userInfo.getId()),
                new ReservationMemo(reservationDto.getReservationMemo()),
                meeting.getId()
        );
    }
}
