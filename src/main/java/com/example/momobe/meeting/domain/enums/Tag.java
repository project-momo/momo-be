package com.example.momobe.meeting.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Tag {
    MENTORING("멘토링"),
    ONLINE("온라인"),
    OFFLINE("오프라인"),
    STUDY("스터디"),
    MEETING("모임"),
    FIVE("5인 이상");

    private final String description;
}
