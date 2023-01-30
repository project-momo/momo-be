package com.example.momobe.reservation.application;

import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.application.MeetingCommonService;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.payment.application.PaymentSaveService;
import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.mapper.PaymentMapper;
import com.example.momobe.reservation.domain.*;
import com.example.momobe.reservation.dto.in.PostReservationDto;
import com.example.momobe.reservation.dto.out.ReservationPaymentDto;
import com.example.momobe.reservation.dto.out.PaymentResponseDto;
import com.example.momobe.reservation.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.momobe.common.exception.enums.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationBookService {
    private final MeetingCommonService meetingCommonService;
    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;
    private final GetReservationsAtSameTimeService getReservationsAtSameTimeService;
    private final PaymentSaveService paymentSaveService;
    private final PaymentMapper paymentMapper;

    public PaymentResponseDto reserve(Long meetingId, PostReservationDto reservationDto, UserInfo userInfo) {
        Meeting meeting = meetingCommonService.getMeeting(meetingId);

        checkAvailabilityOfReservations(meetingId, reservationDto, meeting);
        Reservation reservation = saveReservation(reservationDto, userInfo, meeting);

        return savePaymentInformationOf(userInfo, reservation, meeting);
    }

    private PaymentResponseDto savePaymentInformationOf(UserInfo userInfo, Reservation reservation, Meeting meeting) {
        if (reservation.isPaymentSucceed()) {
            return PaymentResponseDto.freeOrder(meeting, userInfo);
        }

        ReservationPaymentDto paymentInfo = reservationMapper.of(userInfo, reservation, meeting);
        Payment payment = paymentSaveService.save(paymentInfo);

        reservation.setPaymentId(payment.getId());
        return paymentMapper.of(payment);
    }

    private void checkAvailabilityOfReservations(Long meetingId, PostReservationDto reservationDto, Meeting meeting) {
        LocalDate reservationDate = reservationDto.getDateInfo().getReservationDate();
        LocalTime startTime = reservationDto.getDateInfo().getStartTime();
        LocalTime endTime = reservationDto.getDateInfo().getEndTime();

        Long numberOfReservations = countReservationsAtSameTime(meetingId, reservationDate, startTime, endTime);

        if (meeting.isClosed()) {
            throw new ReservationException(CLOSED_MEETING);
        }

        if (!meeting.verifyRemainingReservations(numberOfReservations)) {
            throw new ReservationException(FULL_OF_PEOPLE);
        }

        if (!meeting.verifyReservationSchedule(reservationDate, startTime, endTime)) {
            throw new ReservationException(INVALID_RESERVATION_TIME);
        }

        if (!meeting.matchPrice(reservationDto.getAmount(), startTime, endTime)) {
            throw new ReservationException(AMOUNT_DOSE_NOT_MATCH);
        }
    }
    
    private Long countReservationsAtSameTime(Long meetingId, LocalDate reservationDate, LocalTime startTime, LocalTime endTime) {
        return getReservationsAtSameTimeService.get(meetingId, reservationDate, startTime, endTime);
    }

    private Reservation saveReservation(PostReservationDto reservationDto, UserInfo userInfo, Meeting meeting) {
        Reservation reservation = reservationMapper.of(meeting, reservationDto, userInfo);
        return reservationRepository.save(reservation);
    }
}
