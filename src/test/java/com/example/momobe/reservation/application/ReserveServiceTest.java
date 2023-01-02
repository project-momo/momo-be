package com.example.momobe.reservation.application;

import com.example.momobe.meeting.application.MeetingCommonService;
import com.example.momobe.payment.application.SavePaymentService;
import com.example.momobe.payment.mapper.PaymentMapper;
import com.example.momobe.reservation.domain.CountExistReservationService;
import com.example.momobe.reservation.domain.ReservationRepository;
import com.example.momobe.reservation.mapper.ReservationMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReserveServiceTest {
    @InjectMocks
    ReserveService reserveService;

    @Mock
    MeetingCommonService meetingCommonService;

    @Mock
    ReservationMapper reservationMapper;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    CountExistReservationService countExistReservationService;

    @Mock
    SavePaymentService savePaymentService;

    @Mock
    PaymentMapper paymentMapper;


}