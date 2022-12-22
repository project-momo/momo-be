package com.example.momobe.meeting.dto;

import com.example.momobe.meeting.domain.PricePolicy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
public class MeetingRequestDto {
    @Positive
    @NotNull
    private Long categoryId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @Size(min = 1)
    @NotNull
    private Set<Long> tagIds;
    @Size(min = 1, max = 3)
    @NotNull
    private List<LocationDto> locations;
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
        @Size(min = 1)
        @NotNull
        private List<LocalDateTime> dateTimes;
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
