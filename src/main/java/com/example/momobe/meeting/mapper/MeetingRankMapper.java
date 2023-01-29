package com.example.momobe.meeting.mapper;

import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.dto.out.MeetingRankDto;
import com.example.momobe.meeting.dto.out.MeetingResponseDto;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface MeetingRankMapper {
    default MeetingRankDto of(MeetingResponseDto meeting) {
        return MeetingRankDto.builder()
                .meetingId(meeting.getMeetingId())
                .title(meeting.getTitle())
                .imageUrl(meeting.getHost().getImageUrl())
                .content(meeting.getContent())
                .build();
    }
}
