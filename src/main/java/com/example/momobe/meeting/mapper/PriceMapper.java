package com.example.momobe.meeting.mapper;

import com.example.momobe.meeting.domain.PriceInfo;
import com.example.momobe.meeting.dto.MeetingRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {
    PriceInfo toPrice(MeetingRequestDto.PriceDto priceDto);
}
