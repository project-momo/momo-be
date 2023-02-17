package com.example.momobe.settlement.dao;

import com.example.momobe.common.config.JpaQueryFactoryConfig;
import com.example.momobe.meeting.domain.Address;
import com.example.momobe.meeting.domain.DateTime;
import com.example.momobe.meeting.domain.DateTimeInfo;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.example.momobe.reservation.domain.*;
import com.example.momobe.reservation.domain.enums.ReservationState;
import com.example.momobe.settlement.application.SettlementTransitionService;
import com.example.momobe.settlement.domain.Settlement;
import com.example.momobe.settlement.domain.SettlementRepository;
import com.example.momobe.settlement.dto.out.SettlementResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.CONTENT1;
import static com.example.momobe.meeting.domain.enums.Category.SOCIAL;
import static com.example.momobe.meeting.enums.MeetingConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.jdbc.EmbeddedDatabaseConnection.H2;

@DataJpaTest
@AutoConfigureDataJpa
@AutoConfigureTestDatabase(connection = H2)
@Import(JpaQueryFactoryConfig.class)
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
class SettlementQueryRepositoryTest {
    @Autowired
    private EntityManager em;
    private SettlementQueryRepository settlementQueryRepository;
    @Mock
    private SettlementTransitionService settlementTransitionService;
    Meeting meeting1;
    Reservation reservation;
    @Autowired
    private SettlementRepository settlementRepository;

    @BeforeEach
    void init(){
        settlementQueryRepository = new SettlementQueryRepository(new JPAQueryFactory(em));
    }

    @Test
    @DisplayName("정산 대상 있을 경우")
    void test01(){
        //given
        LocalDate endDate = LocalDate.now().minusDays(3);
        meeting1 = Meeting.builder()
                .title("이거 테스튜")
                .content(CONTENT1)
                .hostId(1L)
                .category(SOCIAL)
                .meetingState(MeetingState.CLOSE)
                .price(15000L)
                .dateTimeInfo(new DateTimeInfo(DatePolicy.ONE_DAY,
                        START_DATE, endDate, START_TIME, END_TIME, MAX_TIME,
                        List.of(new DateTime(LocalDateTime.of(START_DATE, START_TIME)))))
                .personnel(1)
                .address(new Address(List.of(1L, 2L), "추가 주소"))
                .build();
        em.persist(meeting1);
        reservation = Reservation.builder()
                .amount(new Money(15000L))
                .paymentId(null)
                .reservationState(ReservationState.PAYMENT_SUCCESS)
                .meetingId(meeting1.getId())
                .reservationDate(ReservationDate.builder()
                        .date(LocalDate.now().plus(1, ChronoUnit.MONTHS))
                        .startTime(LocalTime.of(10,0))
                        .endTime(LocalTime.of(22,0))
                        .build())
                .reservedUser(new ReservedUser(2L))
                .reservationMemo(new ReservationMemo("이거 테스튜"))
                .build();
        em.persist(reservation);

        //when
        List<SettlementResponseDto.SettlementDto> list =
                settlementQueryRepository.findReservationForMeetingClosed();
        //then

        assertThat(list.get(0).getAmount()).isEqualTo(15000L);
        assertThat(list.get(0).getHost()).isEqualTo(1L);

    }

    @Test
    @DisplayName("정산 대상 없을 경우")
    void test02(){
        //given
        LocalDate endDate = LocalDate.now().plusDays(3);
        meeting1 = Meeting.builder()
                .title("이거 테스튜")
                .content(CONTENT1)
                .hostId(1L)
                .category(SOCIAL)
                .meetingState(MeetingState.CLOSE)
                .price(15000L)
                .dateTimeInfo(new DateTimeInfo(DatePolicy.ONE_DAY,
                        START_DATE, endDate, START_TIME, END_TIME, MAX_TIME,
                        List.of(new DateTime(LocalDateTime.of(START_DATE, START_TIME)))))
                .personnel(1)
                .address(new Address(List.of(1L, 2L), "추가 주소"))
                .build();
        em.persist(meeting1);
        reservation = Reservation.builder()
                .amount(new Money(15000L))
                .paymentId(null)
                .reservationState(ReservationState.PAYMENT_SUCCESS)
                .meetingId(meeting1.getId())
                .reservationDate(ReservationDate.builder()
                        .date(LocalDate.now().plus(1, ChronoUnit.MONTHS))
                        .startTime(LocalTime.of(10,0))
                        .endTime(LocalTime.of(22,0))
                        .build())
                .reservedUser(new ReservedUser(2L))
                .reservationMemo(new ReservationMemo("이거 테스튜"))
                .build();
        em.persist(reservation);

        //when
        List<SettlementResponseDto.SettlementDto> list =
                settlementQueryRepository.findReservationForMeetingClosed();

        //then
        assertThat(list).isEmpty();
    }
}
