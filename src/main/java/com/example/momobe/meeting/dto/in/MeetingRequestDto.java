package com.example.momobe.meeting.dto.in;

import com.example.momobe.meeting.domain.enums.DatePolicy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
public class MeetingRequestDto {
    @NotBlank
    private String category;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @Size(min = 1)
    @NotNull
    private List<String> tags;
    @Valid
    @NotNull
    private AddressDto address;
    @Valid
    @NotNull
    private DateTimeDto dateTime;
    @Min(1)
    private Integer personnel;
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

    /*
    * 프론트 요청으로 인해 예약 생성 시 날짜 제한 임시 해제
    * AUTHOR: eunchanyang
    * DATETIME: 2023/02/09 16:00
    * */
    @Getter
    @Builder
    @AllArgsConstructor(access = PRIVATE)
    public static class DateTimeDto {
        @NotNull
        private DatePolicy datePolicy;
        @NotNull
//        @FutureOrPresent
        private LocalDate startDate;
        @NotNull
//        @FutureOrPresent
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
