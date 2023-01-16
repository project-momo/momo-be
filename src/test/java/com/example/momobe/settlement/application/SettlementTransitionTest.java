//package com.example.momobe.settlement.application;
//
//import com.example.momobe.meeting.domain.DateTimeInfo;
//import com.example.momobe.meeting.domain.Meeting;
//import com.example.momobe.meeting.domain.MeetingRepository;
//import com.example.momobe.payment.domain.Payment;
//import com.example.momobe.payment.domain.PaymentRepository;
//import com.example.momobe.payment.domain.enums.PayState;
//import com.example.momobe.payment.infrastructure.PaymentQueryRepository;
//import com.example.momobe.reservation.domain.CustomReservationRepository;
//import com.example.momobe.reservation.domain.Reservation;
//import com.example.momobe.settlement.domain.Settlement;
//import com.example.momobe.settlement.domain.SettlementRepository;
//import com.example.momobe.user.application.UserFindService;
//import com.example.momobe.user.domain.User;
//import com.example.momobe.user.domain.UserPoint;
//import com.example.momobe.user.domain.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//
//
//@ExtendWith(MockitoExtension.class)
//class SettlementTransitionTest {
//    @InjectMocks
//    private SettlementTransitionService settlementTransitionService;
//    @Mock
//    private SettlementRepository settlementRepository;
//    @Mock
//    private CustomReservationRepository customReservationRepository;
//    @Mock
//    private MeetingRepository meetingRepository;
//    @Mock
//    private PaymentQueryRepository paymentQueryRepository;
//    @Mock
//    private PaymentRepository paymentRepository;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private CheckSettlementService checkSettlementService;
//    @Mock
//    private UserFindService userFindService;
//
//    List<Long> meetingId;
//    Payment successPayment;
//    Payment emptyPayment;
//    Meeting meeting;
//    Reservation reservation;
//    Long amounts;
//    Settlement settlement;
//    User user;
//
//    @BeforeEach
//    void init(){
//        meetingId = List.of(1L, 30L, 40L, 22L);
//        successPayment = Payment.builder()
//                .id(1L)
//                .amount(5000L)
//                .payState(PayState.SUCCESS)
//                .build();
//        emptyPayment = Payment.builder().build();
//        meeting = Meeting.builder()
//                .hostId(1L)
//                .dateTimeInfo(DateTimeInfo.builder().endDate(LocalDate.now().minusDays(3)).build())
//                .build();
//        reservation = Reservation.builder().build();
//        user = User.builder()
//                .userPoint(new UserPoint(5000L))
//                .id(1L)
//                .build();
//        settlement = Settlement.builder()
//                .amount(13000L)
//                .host(meeting.getHostId())
//                .meeting(meeting.getId())
//                .build();
//        amounts = 5000L;
//    }
//
//    @Test
//    @DisplayName("예약 결제 건의 상태가 정산 완료로 변경됐는지 확인")
//    void test01(){
//        //given
//        given(checkSettlementService.checkEndMeetingExist()).willReturn(meetingId);
//        given(paymentQueryRepository.findPaymentByReservationId(anyLong())).willReturn(successPayment);
//        given(paymentRepository.save(any())).willReturn(successPayment);
//        given(meetingRepository.findById(anyLong())).willReturn(Optional.ofNullable(meeting));
//        given(userFindService.verifyUser(anyLong())).willReturn(user);
//        given(userRepository.save(any())).willReturn(user);
//
//        //when
//        settlementTransitionService.transitionOfPayment();
//        //then
//        assertThat(successPayment.getPayState()).isEqualTo(PayState.SETTLEMENT_DONE);
//    }
//
//
//    @Test
//    @DisplayName("userPoint 변경 확인")
//    void test02(){
//        //given
//        given(checkSettlementService.checkEndMeetingExist()).willReturn(meetingId);
//        given(customReservationRepository.findPaymentCompletedReservation(anyLong())).willReturn(List.of(reservation));
//        given(customReservationRepository.findReservationAmounts(anyLong())).willReturn(List.of(amounts));
//        given(paymentQueryRepository.findPaymentByReservationId(anyLong())).willReturn(successPayment);
//        given(paymentRepository.save(any())).willReturn(successPayment);
//        given(meetingRepository.findById(anyLong())).willReturn(Optional.ofNullable(meeting));
//        given(userFindService.verifyUser(anyLong())).willReturn(user);
//        given(userRepository.save(any())).willReturn(user);
//
//        //when
//        settlementTransitionService.transitionOfPayment();
//        //then
////        assertThat().isEqualTo(5000L);
//        assertThat(user.getUserPoint().getPoint()).isEqualTo(15000L);
//    }
//
//    @Test
//    @DisplayName("종료된 모임이 있지만 예약 결제 건 없을 경우 예외 throw")
//    void test03(){
//        //given
//        given(checkSettlementService.checkEndMeetingExist()).willReturn(null);
//        given(customReservationRepository.findPaymentCompletedReservation(anyLong())).willReturn(null);
//        given(customReservationRepository.findReservationAmounts(anyLong())).willReturn(null);
//        given(paymentQueryRepository.findPaymentByReservationId(anyLong())).willReturn(null);
//        given(paymentRepository.save(any())).willReturn(null);
//        given(meetingRepository.findById(anyLong())).willReturn(Optional.ofNullable(meeting));
//        given(userFindService.verifyUser(anyLong())).willReturn(user);
//        given(userRepository.save(any())).willReturn(user);
//
//        //when,then
//        assertThrows(NullPointerException.class, () -> {
//            checkSettlementService.checkEndMeetingExist();
//        });
//
//    }
//}
