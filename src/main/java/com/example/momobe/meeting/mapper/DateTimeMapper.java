package com.example.momobe.meeting.mapper;

import com.example.momobe.meeting.domain.DateTime;
import com.example.momobe.meeting.domain.ReservationStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = ReservationStatus.class)
public interface DateTimeMapper {
    @Mapping(target = "reservationStatus", expression = "java(ReservationStatus.UNRESERVED)")
    DateTime toDateTime(LocalDateTime localDateTime);
}
