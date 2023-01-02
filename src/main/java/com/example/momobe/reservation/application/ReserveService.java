package com.example.momobe.reservation.application;

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
        Meeting meeting = meetingCommonService.findMeetingOrThrowException(meetingId);
        checkAvailabilityReservation(meetingId, reservationDto, meeting);
        Reservation reservation = saveReservation(reservationDto, userInfo, meeting);
    }

    private void checkAvailabilityReservation(Long meetingId, RequestReservationDto reservationDto, Meeting meeting) {
        LocalDate reservationDate = reservationDto.getDateInfo()
                .getReservationDate();
        LocalTime startTime = reservationDto.getDateInfo()
                .getStartTime();
        LocalTime endTime = reservationDto.getDateInfo()
                .getEndTime();

        Long numberOfReservations = countReservationsAtSameTime(meetingId, reservationDate, startTime, endTime);

        if (!meeting.verifyRemainingReservations(numberOfReservations)) {
            throw new ReservationNotPossibleException(FULL_OF_PEOPLE, "예약 정원이 가득 찼습니다.");
        }

        if (!meeting.verifyReservationSchedule(reservationDate, startTime, endTime)) {
            throw new ReservationNotPossibleException(DATA_NOT_FOUND, "존재하지 않는 예약 시간대입니다.");
        }
    }

    private Long countReservationsAtSameTime(Long meetingId, LocalDate reservationDate, LocalTime startTime, LocalTime endTime) {
        return checkExistReservationService.countOf(meetingId, reservationDate, startTime, endTime);
    }

    private Reservation saveReservation(RequestReservationDto reservationDto, UserInfo userInfo, Meeting meeting) {
        Reservation reservation = reservationMapper.of(meeting, reservationDto, userInfo);
        return reservationRepository.save(reservation);
    }
}
