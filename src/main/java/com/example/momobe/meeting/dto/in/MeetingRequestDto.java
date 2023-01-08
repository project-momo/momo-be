package com.example.momobe.meeting.dto.in;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

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
    @NotNull
    private AddressDto address;
    @NotNull
    private DateTimeDto dateTime;
    @Min(1)
    private Integer personnel;
    private String notice;
    @NotNull
    @Min(0)
    private Long price;

    @Getter
    @Builder
    @AllArgsConstructor(access = PRIVATE)
    public static class AddressDto {
        @Size(min = 1)
        @NotNull
        private List<Long> addressIds;
        private String addressInfo;
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
        @Min(1)
        private Integer maxTime;

        private Set<Integer> dayWeeks;
        private List<LocalDate> dates;
    }
}
