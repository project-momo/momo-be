package com.example.momobe.meeting.mapper;

import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.example.momobe.meeting.dto.in.MeetingRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE,
        imports = MeetingState.class, uses = DateTimeMapper.class)
public interface MeetingMapper {
    @Mapping(target = ".", source = "request")
    @Mapping(target = "hostId", source = "hostId")
    @Mapping(target = "tagIds", source = "tagIds")
    @Mapping(target = "dateTimeInfo", source = "request.dateTime")
    @Mapping(target = "meetingState", expression = "java(MeetingState.OPEN)")
    Meeting toMeeting(MeetingRequestDto request, Long hostId, List<Long> tagIds);
}
