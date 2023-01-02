package com.example.momobe.meeting.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Getter
public class MeetingDateTimeDto {
    private final LocalDate date;
    private final String time;

    @QueryProjection
    public MeetingDateTimeDto(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        long gap = startTime.until(endTime, ChronoUnit.HOURS);
        if (gap < 0) gap += 24;
        this.time = startTime + " - " + endTime + " (" + gap + "시간)";
    }
}
