package com.example.momobe.meeting.domain.enums;

import com.example.momobe.meeting.domain.CategoryNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static com.example.momobe.common.exception.enums.ErrorCode.DATA_NOT_FOUND;

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

    public static final Category of(String description) {
        return Arrays.stream(Category.values())
                .filter(c -> c.description.equals(description))
                .findFirst()
                .orElseThrow(() -> new CategoryNotFoundException(DATA_NOT_FOUND));
    }
}
