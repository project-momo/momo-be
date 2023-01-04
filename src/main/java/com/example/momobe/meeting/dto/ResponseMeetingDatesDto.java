package com.example.momobe.meeting.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
@ToString
public class ResponseMeetingDatesDto {
    LocalDateTime dateTime;
    LocalDate date;
    LocalTime time;
    Integer personnel;
    Integer maxTime;
    Integer price;
    String datePolicy;
    Integer currentStaff;
    String category;
    String availability;
}
