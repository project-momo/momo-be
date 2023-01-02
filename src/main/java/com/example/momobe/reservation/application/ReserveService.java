package com.example.momobe.reservation.application;

import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.application.MeetingCommonService;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.payment.application.SavePaymentService;
import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.mapper.PaymentMapper;
import com.example.momobe.reservation.domain.*;
import com.example.momobe.reservation.domain.enums.ReservationState;
import com.example.momobe.reservation.dto.in.RequestReservationDto;
import com.example.momobe.reservation.dto.out.ReservationPaymentDto;
import com.example.momobe.reservation.dto.out.PaymentResponseDto;
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
    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;
    private final CountExistReservationService countExistReservationService;
    private final SavePaymentService savePaymentService;
    private final PaymentMapper paymentMapper;

    public PaymentResponseDto reserve(Long meetingId, RequestReservationDto reservationDto, UserInfo userInfo) {
        Meeting meeting = meetingCommonService.findMeetingOrThrowException(meetingId);

        checkAvailabilityOfReservations(meetingId, reservationDto, meeting);
        Reservation reservation = saveReservation(reservationDto, userInfo, meeting);

        return savePaymentInformationOf(userInfo, reservation, meeting);
    }

    private PaymentResponseDto savePaymentInformationOf(UserInfo userInfo, Reservation reservation, Meeting meeting) {
        if (reservation.checkReservationState() == ReservationState.PAYMENT_SUCCESS) {
            return PaymentResponseDto.freeOrder(meeting.getTitle());
        }

        ReservationPaymentDto paymentInfo = reservationMapper.of(userInfo, reservation, meeting);
        Payment payment = savePaymentService.save(paymentInfo);
        return paymentMapper.of(payment);
    }

    private void checkAvailabilityOfReservations(Long meetingId, RequestReservationDto reservationDto, Meeting meeting) {
        LocalDate reservationDate = reservationDto.getDateInfo().getReservationDate();
        LocalTime startTime = reservationDto.getDateInfo().getStartTime();
        LocalTime endTime = reservationDto.getDateInfo().getEndTime();

        Long numberOfReservations = countReservationsAtSameTime(meetingId, reservationDate, startTime, endTime);

        if (meeting.isClosed()) {
            throw new ReservationNotPossibleException(CLOSED_MEETING);
        }

        if (!meeting.verifyRemainingReservations(numberOfReservations)) {
            throw new ReservationNotPossibleException(FULL_OF_PEOPLE);
        }

        if (!meeting.verifyReservationSchedule(reservationDate, startTime, endTime)) {
            throw new ReservationNotPossibleException(INVALID_RESERVATION_TIME);
        }

        if (!meeting.matchPrice(reservationDto.getAmount(), startTime, endTime)) {
            throw new ReservationNotPossibleException(AMOUNT_DOSE_NOT_MATCH);
        }
    }

    private Long countReservationsAtSameTime(Long meetingId, LocalDate reservationDate, LocalTime startTime, LocalTime endTime) {
        return countExistReservationService.countOf(meetingId, reservationDate, startTime, endTime);
    }

    private Reservation saveReservation(RequestReservationDto reservationDto, UserInfo userInfo, Meeting meeting) {
        Reservation reservation = reservationMapper.of(meeting, reservationDto, userInfo);
        return reservationRepository.save(reservation);
    }
}
