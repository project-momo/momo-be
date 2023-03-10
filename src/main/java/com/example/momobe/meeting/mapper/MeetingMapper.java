package com.example.momobe.meeting.mapper;

import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.example.momobe.meeting.dto.in.MeetingRequestDto;
import com.example.momobe.meeting.dto.in.MeetingUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE,
        imports = {MeetingState.class, Category.class}, uses = DateTimeMapper.class)
public interface MeetingMapper {
    @Mapping(target = ".", source = "request")
    @Mapping(target = "hostId", source = "hostId")
    @Mapping(target = "tagIds", source = "tagIds")
    @Mapping(target = "dateTimeInfo", source = "request.dateTime")
    @Mapping(target = "meetingState", expression = "java(MeetingState.OPEN)")
    @Mapping(target = "category", expression = "java(Category.of(request.getCategory()))")
    Meeting toMeeting(MeetingRequestDto request, Long hostId, List<Long> tagIds);

    @Mapping(target = ".", source = "request")
    @Mapping(target = "hostId", source = "hostId")
    @Mapping(target = "tagIds", source = "tagIds")
    @Mapping(target = "category", expression = "java(Category.of(request.getCategory()))")
    Meeting toMeeting(MeetingUpdateDto request, Long hostId, List<Long> tagIds);
}
