package com.example.momobe.meeting.dto;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.PricePolicy;
import com.example.momobe.meeting.domain.enums.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
public class MeetingRequestDto {
    @NotNull
    private Category category;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @Size(min = 1)
    @NotNull
    private List<Tag> tags;
    @Size(min = 1, max = 3)
    @NotNull
    private List<LocationDto> locations;
    @NotNull
    private DateTimeDto dateTime;
    @NotNull
    private PriceDto priceInfo;
    private String notice;

    @Getter
    @Builder
    @AllArgsConstructor(access = PRIVATE)
    public static class LocationDto {
        @NotNull
        private String address1;
        private String address2;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = PRIVATE)
    public static class DateTimeDto {
        @NotNull
        private DatePolicy datePolicy;
        @NotNull
        private LocalDate startDate;
        @NotNull
        private LocalDate endDate;
        @NotNull
        private LocalTime startTime;
        @NotNull
        private LocalTime endTime;

        private List<LocalDate> dates;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = PRIVATE)
    public static class PriceDto {
        @NotNull
        private PricePolicy pricePolicy;
        @NotNull
        private Long price;
    }
}
