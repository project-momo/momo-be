package com.example.momobe.meeting.mapper;

import com.example.momobe.meeting.domain.DateTime;
import com.example.momobe.meeting.domain.DateTimeInfo;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.ReservationState;
import com.example.momobe.meeting.dto.MeetingRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE,
        imports = ReservationState.class)
public interface DateTimeMapper {
    @Mapping(target = ".", source = ".")
    @Mapping(target = "dateTimes", source = ".")
    DateTimeInfo toDateTimeInfo(MeetingRequestDto.DateTimeDto dateTimeDto);

    default List<DateTime> toDateTime(MeetingRequestDto.DateTimeDto dateTimeDto) {
        ArrayList<DateTime> dateTimes = new ArrayList<>();
        int startTime = dateTimeDto.getStartTime().getHour();
        int endTime = dateTimeDto.getEndTime().getHour();

        if (dateTimeDto.getDatePolicy() == DatePolicy.FREE) {
            dateTimeDto.getDates()
                    .forEach(date -> addGeneratedDateTime(dateTimes, date, startTime, endTime));
        }

        return dateTimes;
    }

    default void addGeneratedDateTime(ArrayList<DateTime> dateTimes, LocalDate date, int startTime, int endTime) {
        for (int i = startTime; i <= endTime; i++) {
            dateTimes.add(new DateTime(LocalDateTime.of(date, LocalTime.of(i, 0))));
        }
    }
}
