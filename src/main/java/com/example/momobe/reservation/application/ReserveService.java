package com.example.momobe.reservation.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.application.MeetingCommonService;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.reservation.domain.*;
import com.example.momobe.reservation.dto.in.RequestReservationDto;
import com.example.momobe.reservation.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.momobe.common.exception.enums.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ReserveService {
    private final MeetingCommonService meetingCommonService;
    private final CheckExistReservationService checkExistReservationService;
    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;

    public void reserve(Long meetingId, RequestReservationDto reservationDto, UserInfo userInfo) {
        Long numberOfReservations = checkNumberOfReservationsAtSameTime(meetingId, reservationDto);
        Meeting meeting = meetingCommonService.findMeetingOrThrowException(meetingId);

        if (!meeting.checkIfCanReservation(numberOfReservations)) {
            throw new ReservationNotPossibleException(FULL_OF_PEOPLE);
        }

        Reservation reservation = saveReservation(reservationDto, userInfo, meeting);

    }

    private Reservation saveReservation(RequestReservationDto reservationDto, UserInfo userInfo, Meeting meeting) {
        Reservation reservation = reservationMapper.of(meeting, reservationDto, userInfo);
        return reservationRepository.save(reservation);
    }

    private Long checkNumberOfReservationsAtSameTime(Long meetingId, RequestReservationDto reservationDto) {
        LocalDate reservationDate = reservationDto.getDateInfo()
                .getReservationDate();
        LocalTime startTime = reservationDto.getDateInfo()
                .getStartTime();
        LocalTime endTime = reservationDto.getDateInfo()
                .getEndTime();

        return checkExistReservationService.countOf(meetingId, reservationDate, startTime, endTime);
    }
}
