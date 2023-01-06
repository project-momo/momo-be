//package com.example.momobe.settlement.application;
//
//import com.example.momobe.meeting.dao.MeetingQueryRepository;
//import com.example.momobe.meeting.domain.Meeting;
//import com.example.momobe.reservation.domain.CustomReservationRepository;
//import com.example.momobe.reservation.domain.Reservation;
//import com.example.momobe.settlement.domain.Settlement;
//import com.example.momobe.settlement.infrastructure.SettlementRepository;
//import com.example.momobe.user.application.UserFindService;
//import com.example.momobe.user.domain.UserPoint;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class SettlementCommonService {
//    private final SettlementRepository repository;
//    private final MeetingQueryRepository meetingQueryRepository;
//    private final CustomReservationRepository customReservationRepository;
//    private final UserFindService userFindService;
//
//    //종료 미팅 조회
//    public List<Long> findEndMeetingId(){
//        return meetingQueryRepository.findMeetingClosedBefore3days();
//    }
//
//
//    public Settlement transitionOfPayment(Long payment,Long hostId){
//        findEndMeetingId().forEach(
//                x -> {
//                    List<Reservation> reservations = customReservationRepository.findPaymentCompletedReservation(x);
//                    reservations
//                }
//        );
//
//
//        UserPoint userPoint = userFindService.verifyUser(hostId).getUserPoint();
//        Settlement settlement = Settlement.builder()
//                .host(hostId)
//                .point(userPoint.plus(payment))
//                .build();
//        repository.save(settlement);
//        return settlement;
//    }
//}
//
//
///**
// * 대금 전환 로직
// *  1. 엔드타임 조회시점으로부터 3일 전 모임까지 조회 -> meeting query repository에 종료일 기준 조회 쿼리 만들어서 조회 (아이디만 조회)
// *  2. 미팅 아이디로 해당 미팅의 완료 상태인 예약만 조회
// *  3. 예약의 결제 금액 총 합산하여 정산 데이터 생성
// *  4. 동시에 유저 포인트 변경
// * */
