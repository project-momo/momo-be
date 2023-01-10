package com.example.momobe.meeting.dao;

import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.dto.out.MeetingHostResponseDto;
import com.example.momobe.meeting.dto.out.MeetingInfoDto;
import com.example.momobe.meeting.dto.out.MeetingResponseDto;
import com.example.momobe.reservation.domain.enums.ReservationState;

import java.time.LocalDate;
import java.util.*;

public class MeetingInfoUtil {
    Map<Long, List<MeetingHostResponseDto.RequestDto>> requestMaps = new LinkedHashMap<>();
    Map<Long, List<MeetingHostResponseDto.RequestConfirmedDto>> confirmedMaps = new LinkedHashMap<>();
    Map<Long, List<String>> addressesMaps = new LinkedHashMap<>();
    Map<Long, List<Integer>> dayWeeksMaps = new LinkedHashMap<>();
    Map<Long, List<LocalDate>> datesMaps = new LinkedHashMap<>();
    Map<Long, MeetingResponseDto.DateTimeDto> dateInfoMap = new LinkedHashMap<>();

    public MeetingInfoUtil(List<? extends MeetingResponseDto> dtos) {
        dtos.forEach(dto -> {
            requestMaps.put(dto.getMeetingId(), new ArrayList<>());
            confirmedMaps.put(dto.getMeetingId(), new ArrayList<>());
            addressesMaps.put(dto.getMeetingId(), new ArrayList<>());
            dayWeeksMaps.put(dto.getMeetingId(), null);
            datesMaps.put(dto.getMeetingId(), null);
            dateInfoMap.put(dto.getMeetingId(), dto.getDateTime());
        });
    }

    public void updateReservations(Map<Long, MeetingInfoDto> meetingInfoDtoMap) {
        meetingInfoDtoMap.forEach(
                (meetingId, meetingInfoDto) ->
                        meetingInfoDto.getReservations().forEach(
                                reservationDto -> {
                                    if (reservationDto.getStartTime() == null) return;
                                    if (reservationDto.getReservationState() == ReservationState.ACCEPT) {
                                        confirmedMaps.get(meetingId).add(new MeetingHostResponseDto.RequestConfirmedDto(reservationDto));
                                    } else {
                                        requestMaps.get(meetingId).add(new MeetingHostResponseDto.RequestDto(reservationDto));
                                    }
                                }
                        ));
    }

    public void updateAddressesAndDateTimes(Map<Long, MeetingInfoDto> meetingInfoDtoMap) {
        meetingInfoDtoMap.forEach(
                (meetingId, meetingInfoDto) -> {
                    addressesMaps.put(meetingId, new ArrayList<>(meetingInfoDto.getAddresses()));
                    MeetingResponseDto.DateTimeDto dateTimeDto = dateInfoMap.get(meetingId);

                    if (dateTimeDto.getDatePolicy() == DatePolicy.FREE) {
                        LinkedHashSet<LocalDate> set = new LinkedHashSet<>();
                        meetingInfoDto.getDateTimes().forEach(dateTime -> {
                            if (dateTime != null)
                                set.add(dateTime.toLocalDate());
                        });
                        datesMaps.put(meetingId, new ArrayList<>(set));
                    } else if (dateTimeDto.getDatePolicy() == DatePolicy.PERIOD) {
                        TreeSet<Integer> set = new TreeSet<>();
                        meetingInfoDto.getDateTimes().forEach(dateTime -> {
                            if (dateTime != null)
                                set.add(dateTime.getDayOfWeek().getValue());
                        });
                        dayWeeksMaps.put(meetingId, new ArrayList<>(set));
                    }
                }
        );
    }

    public void initMeetingHostResponseDto(List<MeetingHostResponseDto> dtos) {
        dtos.forEach(dto -> dto.init(
                addressesMaps.get(dto.getMeetingId()),
                dayWeeksMaps.get(dto.getMeetingId()),
                datesMaps.get(dto.getMeetingId()),
                new MeetingHostResponseDto.ApplicationDto(
                        requestMaps.get(dto.getMeetingId()),
                        confirmedMaps.get(dto.getMeetingId())
                )));
    }

    public void initMeetingResponseDto(List<? extends MeetingResponseDto> dtos) {
        dtos.forEach(dto -> dto.init(
                addressesMaps.get(dto.getMeetingId()),
                dayWeeksMaps.get(dto.getMeetingId()),
                datesMaps.get(dto.getMeetingId())));
    }
}
