package com.example.momobe.settlement.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.MeetingNotFoundException;
import com.example.momobe.meeting.domain.MeetingRepository;
import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.domain.PaymentRepository;
import com.example.momobe.payment.domain.enums.PayState;
import com.example.momobe.payment.infrastructure.PaymentQueryRepository;
import com.example.momobe.reservation.domain.CustomReservationRepository;
import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.settlement.domain.Settlement;
import com.example.momobe.settlement.domain.SettlementRepository;
import com.example.momobe.user.application.UserFindService;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.domain.UserPoint;
import com.example.momobe.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableAsync
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
                                Meeting meeting = meetingRepository.findById(x).orElseThrow(() -> new MeetingNotFoundException(ErrorCode.DATA_NOT_FOUND));
                                User user = userFindService.verifyUser(meeting.getHostId());
                                UserPoint userPoint = user.getUserPoint();
                                user.changeUserPoint(userPoint.plus(amounts));
                                Settlement settlement = Settlement.builder()
                                        .host(meeting.getHostId())
                                        .amount(amounts)
                                        .meeting(x)
                                        .reservation(reservationId)
                                        .build();
                                userRepository.save(user);
                            }
                    );
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
