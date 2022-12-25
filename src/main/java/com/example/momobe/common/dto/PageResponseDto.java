package com.example.momobe.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponseDto<T> {
    private final List<T> content;
    private final PageInfo pageInfo;

    public PageResponseDto(Page<T> page) {
        this.content = page.getContent();
        this.pageInfo = PageInfo.of(page);
    }

    public static <T> PageResponseDto<T> of(Page<T> page) {
        return new PageResponseDto<>(page);
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PageInfo {
        private final int page;
        private final int size;
        private final long totalElements;
        private final int totalPages;

        public static <T> PageInfo of(Page<T> page) {
            return new PageInfo(page.getNumber() + 1,
                    page.getSize(), page.getTotalElements(), page.getTotalPages());
        }

    }

}
