package com.example.momobe.meeting.dto.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
public class MeetingUpdateDto {
    private String category;
    private String title;
    private String content;
    private List<String> tags;
    private MeetingRequestDto.AddressDto address;
    private Integer personnel;
    private Long price;
}
