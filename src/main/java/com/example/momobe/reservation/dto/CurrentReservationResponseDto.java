package com.example.momobe.reservation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class CurrentReservationResponseDto {
    private HostInfo hostInfo;
    private MeetingInfo meetingInfo;
    private String reservationState;
    private Long dueDateCount;

    public static class HostInfo{
        private String nickname;
        private String email;
    }
    public static class MeetingInfo{
        private String title;
        private String address;
        private String memo;
    }
}
