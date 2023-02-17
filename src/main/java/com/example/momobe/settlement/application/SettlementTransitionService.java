package com.example.momobe.settlement.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.reservation.domain.ReservationException;
import com.example.momobe.settlement.domain.Settlement;
import com.example.momobe.settlement.domain.SettlementRepository;
import com.example.momobe.settlement.domain.enums.PointUsedType;
import com.example.momobe.settlement.domain.enums.SettlementState;
import com.example.momobe.settlement.dao.SettlementQueryRepository;
import com.example.momobe.settlement.domain.exception.CanNotSettleException;
import com.example.momobe.settlement.dto.out.SettlementResponseDto;
import com.example.momobe.user.application.UserFindService;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SettlementTransitionService {
    private final SettlementRepository settlementRepository;
    private final SettlementQueryRepository settlementQueryDslRepository;
    private final UserFindService userFindService;
    private final UserRepository userRepository;


    @Scheduled(cron = "0 0 10 1 * ?")
    public void transitionOfPayment() {
        List<SettlementResponseDto.SettlementDto> settlements = settlementQueryDslRepository.findReservationForMeetingClosed();
        if(settlements.isEmpty()) throw new CanNotSettleException(ErrorCode.CAN_NOT_FOUND_SETTLEMENT);
        settlements.forEach(
                x -> {
                    if(settlementRepository.findByReservationId(x.getReservationId())==null){
                        Settlement settlement = Settlement.builder()
                                .host(x.getHost())
                                .paymentId(x.getPaymentId())
                                .meetingId(x.getMeetingId())
                                .reservationId(x.getReservationId())
                                .amount(x.getAmount())
                                .build();
                        User user = userFindService.verifyUser(x.getHost());
                        user.changeUserPoint(user.plusUserPoint(x.getAmount(), PointUsedType.SETTLEMENT));
                        userRepository.save(user);
                        settlement.changeSettlementState(settlement,SettlementState.DONE);
                        settlementRepository.save(settlement);
                    }
                });
    }
}
