package com.example.momobe.meeting.mapper;

import com.example.momobe.meeting.domain.DateTime;
import com.example.momobe.meeting.domain.DateTimeInfo;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.ReservationStatus;
import com.example.momobe.meeting.dto.MeetingRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", imports = ReservationStatus.class)
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
                    .forEach(date -> dateTimes.add(generateDateTime(date, startTime, endTime)));
        } else if (dateTimeDto.getDatePolicy() == DatePolicy.PERIOD) {
            dateTimeDto.getStartDate().datesUntil(dateTimeDto.getEndDate().plusDays(1))
                    .forEach(date -> {
                        if (dateTimeDto.getDayWeeks().contains(date.getDayOfWeek().getValue()))
                            dateTimes.add(generateDateTime(date, startTime, endTime));
                    });
        } else {
            dateTimes.add(generateDateTime(dateTimeDto.getStartDate(), startTime, endTime));
        }

        return dateTimes;
    }

    default DateTime generateDateTime(LocalDate date, int startTime, int endTime) {
        List<LocalTime> times = new ArrayList<>();
        for (int i = startTime; i <= endTime; i++) {
            times.add(LocalTime.of(i, 0));
        }
        return new DateTime(date, times);
    }
}
