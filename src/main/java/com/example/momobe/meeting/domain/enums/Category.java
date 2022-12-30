package com.example.momobe.meeting.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    LIFESTYLE("라이프스타일"),
    MEDIA("미디어"),
    EDU("교육"),
    SOCIAL("소셜"),
    DESIGN("디자인"),
    DEVELOP("개발"),
    FINANCE("금융"),
    AI("인공지능");

    private final String description;
}
