package com.example.momobe.meeting.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MeetingState {
    OPEN("모집중"), CLOSE("모집 완료");

    private final String description;
}
