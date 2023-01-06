package com.example.momobe.meeting.dto;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.example.momobe.question.dto.out.ResponseQuestionDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Getter
public class MeetingDetailResponseDto extends MeetingResponseDto {
    private final List<String> tags;
    private List<ResponseQuestionDto> questions;

    @QueryProjection
    public MeetingDetailResponseDto(Long meetingId, Category category, Long hostId, String hostNickname, String hostImageUrl, String hostEmail, String title, String content, String addressInfo, MeetingState meetingState, DatePolicy datePolicy, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, Integer maxTime, Long price,
                                    Set<String> addresses, Set<String> tags, List<LocalDateTime> dateTimes) {
        super(meetingId, category, hostId, hostNickname, hostImageUrl, hostEmail, title, content, addressInfo, meetingState, datePolicy, startDate, endDate, startTime, endTime, maxTime, price);

        List<Integer> dayWeeks = new ArrayList<>();
        List<LocalDate> dates = new ArrayList<>();

        if (datePolicy == DatePolicy.FREE) {
            LinkedHashSet<LocalDate> set = new LinkedHashSet<>();
            dateTimes.forEach(dateTime -> {
                if (dateTime != null)
                    set.add(dateTime.toLocalDate());
            });
            dates = new ArrayList<>(set);
        } else if (datePolicy == DatePolicy.PERIOD) {
            TreeSet<Integer> set = new TreeSet<>();
            dateTimes.forEach(dateTime -> {
                if (dateTime != null)
                    set.add(dateTime.getDayOfWeek().getValue());
            });
            dayWeeks = new ArrayList<>(set);
        }

        this.tags = new ArrayList<>(tags);
        this.init(new ArrayList<>(addresses), dayWeeks, dates);
    }

    public void init(List<ResponseQuestionDto> questions) {
        this.questions = questions;
    }
}
