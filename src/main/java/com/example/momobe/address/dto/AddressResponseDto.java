package com.example.momobe.address.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
public class AddressResponseDto {
    private final String si;
    private final List<AddressDto> gu;

    @QueryProjection
    public AddressResponseDto(String si, List<AddressDto> gu) {
        this.si = si;
        this.gu = gu;
    }

    @Getter
    public static class AddressDto {
        private final Long id;
        private final String name;

        @QueryProjection
        public AddressDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
