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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationBookService {
    private final MeetingCommonService meetingCommonService;
    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;
    private final PaymentSaveService paymentSaveService;
    private final PaymentMapper paymentMapper;
    private final ReservationValidateService reservationValidateService;

    public PaymentResponseDto reserve(Long meetingId, PostReservationDto reservationDto, UserInfo userInfo) {
        Meeting meeting = meetingCommonService.getMeeting(meetingId);

        reservationValidateService.validate(meetingId, reservationDto, meeting, userInfo);
        Reservation reservation = saveReservation(reservationDto, userInfo, meeting);

        return getPaymentResponse(userInfo, reservation, meeting);
    }

    private PaymentResponseDto getPaymentResponse(UserInfo userInfo, Reservation reservation, Meeting meeting) {
        if (reservation.isPaymentSucceed()) {
            return PaymentResponseDto.freeOrder(meeting, userInfo);
        }

        ReservationPaymentDto paymentInfo = reservationMapper.of(userInfo, reservation, meeting);
        Payment payment = paymentSaveService.save(paymentInfo);

        reservation.setPaymentId(payment.getId());
        return paymentMapper.of(payment);
    }

    private Reservation saveReservation(PostReservationDto reservationDto, UserInfo userInfo, Meeting meeting) {
        Reservation reservation = reservationMapper.of(meeting, reservationDto, userInfo);
        return reservationRepository.save(reservation);
    }
}
