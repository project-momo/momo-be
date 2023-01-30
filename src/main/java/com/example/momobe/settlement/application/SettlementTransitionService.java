package com.example.momobe.settlement.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.MeetingException;
import com.example.momobe.meeting.domain.MeetingRepository;
import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.domain.PaymentRepository;
import com.example.momobe.payment.domain.enums.PayState;
import com.example.momobe.payment.infrastructure.PaymentQueryRepository;
import com.example.momobe.reservation.domain.CustomReservationRepository;
import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.settlement.domain.Settlement;
import com.example.momobe.settlement.domain.SettlementRepository;
import com.example.momobe.settlement.domain.enums.PointUsedType;
import com.example.momobe.user.application.UserFindService;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SettlementTransitionService {
    private final SettlementRepository settlementRepository;
    private final CustomReservationRepository customReservationRepository;
    private final PaymentQueryRepository paymentQueryRepository;
    private final PaymentRepository paymentRepository;
    private final CheckSettlementService checkSettlementService;
    private final UserFindService userFindService;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;


    @Scheduled(cron = "0 0 0 * * *")
    public void transitionOfPayment() {
        List<Long> meetingId = checkSettlementService.checkEndMeetingExist();
        meetingId.forEach(
                x -> {
                    Long amounts = customReservationRepository.findReservationAmounts(x)
                            .stream().mapToLong(m -> m).sum();
                    List<Reservation> reservations = customReservationRepository.findPaymentCompletedReservation(x);
                    List<Long> reservationId = reservations.stream().map(Reservation::getId).collect(Collectors.toList());
                    reservationId.forEach(r -> {
                                Payment payment = paymentQueryRepository.findPaymentByReservationId(r);
                                if (payment != null) {
                                    payment.changePaymentState(PayState.SETTLEMENT_DONE);
                                    paymentRepository.save(payment);
                                }
                                Meeting meeting = meetingRepository.findById(x).orElseThrow(() -> new MeetingException(ErrorCode.DATA_NOT_FOUND));
                                User user = userFindService.verifyUser(meeting.getHostId());
                                user.changeUserPoint(user.plusUserPoint(amounts, PointUsedType.SETTLEMENT));
                                Settlement settlement = Settlement.builder()
                                        .host(meeting.getHostId())
                                        .amount(amounts)
                                        .meeting(x)
                                        .reservation(reservationId)
                                        .build();
                                settlementRepository.save(settlement);
                                userRepository.save(user);
                            }
                    );
                });
    }
}
