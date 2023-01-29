package com.example.momobe.settlement.integration;

import com.example.momobe.meeting.dao.MeetingQueryRepository;
import com.example.momobe.reservation.domain.CustomReservationRepository;
import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.settlement.application.CheckSettlementService;
import com.example.momobe.settlement.application.SettlementTransitionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
public class SettlementIntegrationTest {
    @InjectMocks
    private SettlementTransitionService settlementTransitionService;
    @Mock
    private CheckSettlementService checkSettlementService;
    @Mock
    private MeetingQueryRepository meetingQueryRepository;
    @Mock
    private CustomReservationRepository customReservationRepository;

    List<Long> meetingId;
    List<Long> meetingId2;
    Reservation emptyReservation;


    @BeforeEach
    void init(){
        meetingId = List.of(1L, 30L, 40L, 22L);
        meetingId2 = new ArrayList<>();
    }

    /** 반환값과 컨트롤러가 없어서 통합테스트는 생략하고 단위 테스트로 대체합니다.*/

//    @Test
//    @DisplayName("오늘 날짜 기준 정산할 모임 건 없으면 예외 throw")
//    void test01() {
//        //given
//        given(meetingQueryRepository.findMeetingClosedBefore3days()).willReturn(meetingId2);
//
//        //when,then
//        assertThrows(NotFoundEndMeetingException.class, () -> {
//            checkSettlementService.checkEndMeetingExist();
//        });
//
//        //then
//        assertThat(meetingId2.size()).isZero();
//    }
//
//    @Test
//    @DisplayName("종료된 모임은 있으나 예약 결제 건이 없을 경우 예외 throw")
//    void test01(){
//        //given
//        given(checkSettlementService.checkEndMeetingExist()).willReturn(meetingId);
//        given(customReservationRepository.findPaymentCompletedReservation(anyLong())).willReturn(List.of(emptyReservation));
//
//        //when,then
//        assertThrows(ReservationNotPossibleException.class, () -> {
//            settlementTransitionService.transitionOfPayment();
//        });
//    }
//
//    @Test
//    @DisplayName("종료된 모임이 있고 예약 결제 건 있을 경우 정산 정상 생성")
//    void test02(){
//        //given
//        given(checkSettlementService.checkEndMeetingExist()).willReturn(meetingId);
//        given(customReservationRepository.findPaymentCompletedReservation(anyLong())).willReturn(List.of(emptyReservation));
//
//        //when
//        settlementTransitionService.transitionOfPayment();
//
//        //then
//        //정산 금액, 정산 호스트 확인
//        assertThat().isEqualTo(1000L);
//
//    }
}
