package com.example.momobe.settlement.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.meeting.domain.MeetingRepository;
import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.domain.PaymentRepository;
import com.example.momobe.payment.domain.enums.PayState;
import com.example.momobe.payment.infrastructure.PaymentQueryRepository;
import com.example.momobe.reservation.domain.CustomReservationRepository;
import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.reservation.domain.enums.ReservationState;
import com.example.momobe.settlement.domain.NotFoundEndMeetingException;
import com.example.momobe.settlement.domain.Settlement;
import com.example.momobe.settlement.domain.SettlementRepository;
import com.example.momobe.settlement.domain.enums.PointUsedType;
import com.example.momobe.settlement.infrastructure.SettlementQueryRepository;
import com.example.momobe.settlement.infrastructure.SettlementQuerydslRepository;
import com.example.momobe.user.application.UserFindService;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SettlementTransitionService {
    private final SettlementRepository settlementRepository;
    private final SettlementQueryRepository settlementQueryRepository;
    private final SettlementQuerydslRepository settlementQuerydslRepository;
    private final CustomReservationRepository customReservationRepository;
    private final PaymentQueryRepository paymentQueryRepository;
    private final PaymentRepository paymentRepository;
    private final CheckSettlementService checkSettlementService;
    private final UserFindService userFindService;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;


    @Scheduled(cron = "30 * * * * *")
    public void transitionOfPayment() {
        List<Reservation> reservations = settlementQuerydslRepository.findReservationForMeetingClosedBefore3days();
        if(reservations.isEmpty()) throw new NotFoundEndMeetingException(ErrorCode.CAN_NOT_FOUND_END_MEETING);
        reservations.forEach(
                x -> {
                    Payment payment = settlementQueryRepository.findPaymentOfReservation(x.getPaymentId());
                    if (payment != null) {
                        payment.changePaymentState(PayState.SETTLEMENT_DONE);
                        paymentRepository.save(payment);
                    }
                    Long host = settlementQueryRepository.findHostOfReservation(x.getMeetingId());
                    User user = userFindService.verifyUser(host);
                    user.changeUserPoint(user.plusUserPoint(x.getAmount().getWon(), PointUsedType.SETTLEMENT));

                    Settlement settlement = Settlement.builder()
                            .host(host)
                            .amount(x.getAmount().getWon())
                            .meeting(x.getMeetingId())
                            .reservation(x.getId())
                            .build();
                    settlementRepository.save(settlement);
                    userRepository.save(user);
                });
    }
}


/**
 * 대금 전환 로직
 * 1. 엔드타임 조회시점으로부터 3일 전 모임까지 조회 -> meeting query repository에 종료일 기준 조회 쿼리 만들어서 조회 (아이디만 조회)
 * 2. 미팅 아이디로 해당 미팅의 완료 상태인 예약만 조회
 * 3. 예약의 결제 금액 총 합산하여 정산 데이터 생성
 * 4. 동시에 유저 포인트 변경
 */
