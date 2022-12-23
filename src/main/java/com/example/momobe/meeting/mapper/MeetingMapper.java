package com.example.momobe.meeting.mapper;

import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.enums.MeetingStatus;
import com.example.momobe.meeting.dto.MeetingRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        imports = MeetingStatus.class, uses = {LocationMapper.class, DateTimeMapper.class, PriceMapper.class})
public interface MeetingMapper {
    @Mapping(target = ".", source = "request")
    @Mapping(target = ".", source = "hostId")
    @Mapping(target = "meetingStatus", expression = "java(MeetingStatus.OPEN)")
    Meeting toMeeting(MeetingRequestDto request, Long hostId);
}
