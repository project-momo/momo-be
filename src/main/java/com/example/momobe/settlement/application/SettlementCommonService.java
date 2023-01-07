package com.example.momobe.settlement.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.meeting.dao.MeetingQueryRepository;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.MeetingNotFoundException;
import com.example.momobe.meeting.domain.MeetingRepository;
import com.example.momobe.reservation.domain.CustomReservationRepository;
import com.example.momobe.settlement.domain.Settlement;
import com.example.momobe.settlement.domain.emun.SettlementState;
import com.example.momobe.settlement.infrastructure.SettlementRepository;
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

@Service
@EnableAsync
@RequiredArgsConstructor
@Transactional
public class SettlementCommonService {
    private final SettlementRepository repository;
    private final MeetingQueryRepository meetingQueryRepository;
    private final CustomReservationRepository customReservationRepository;
    private final UserFindService userFindService;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;

    //종료 미팅 조회
    public List<Long> findEndMeetingId(){
        return meetingQueryRepository.findMeetingClosedBefore3days();
    }


    @Scheduled(cron = "010***")
    public void transitionOfPayment(){
        List<Long> meetingId = findEndMeetingId();
        meetingId.forEach(
                x -> {
                    Long amounts = customReservationRepository.findReservationAmounts(x)
                            .stream().mapToLong(m->m).sum();

                    //예약건 다 꺼내서 합산해서 정산 데이터 생성
                    //유저포인트 변경

                    Meeting meeting = meetingRepository.findById(x).orElseThrow(() -> new MeetingNotFoundException(ErrorCode.DATA_NOT_FOUND));
                    User user = userFindService.verifyUser(meeting.getHostId());
                    UserPoint userPoint = user.getUserPoint();
                    Settlement settlement = Settlement.builder()
                            .host(meeting.getHostId())
                            .point(amounts)
                            .userPoint(userPoint.plus(amounts))
                            .state(SettlementState.DONE)
                            .amount(amounts)

                            .build();
                    userRepository.save(user);
                    repository.save(settlement);
                }
        );
    }
}


/**
 * 대금 전환 로직
 *  1. 엔드타임 조회시점으로부터 3일 전 모임까지 조회 -> meeting query repository에 종료일 기준 조회 쿼리 만들어서 조회 (아이디만 조회)
 *  2. 미팅 아이디로 해당 미팅의 완료 상태인 예약만 조회
 *  3. 예약의 결제 금액 총 합산하여 정산 데이터 생성
 *  4. 동시에 유저 포인트 변경
 * */
