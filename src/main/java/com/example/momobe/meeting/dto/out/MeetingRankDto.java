package com.example.momobe.meeting.dto.out;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetingRankDto {
    private Long meetingId;
    private String title;
    private String content;
    private String imageUrl;
}
