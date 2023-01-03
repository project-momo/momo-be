package com.example.momobe.reservation.domain;

import com.example.momobe.common.config.JpaQueryFactoryConfig;
import com.example.momobe.reservation.infrastructure.CustomJpaQueryRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static java.time.temporal.ChronoUnit.*;

@DataJpaTest
@AutoConfigureDataJpa
@Import(JpaQueryFactoryConfig.class)
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomReservationRepositoryTest {
    @Autowired
    EntityManager entityManager;

    CustomReservationRepository reservationRepository;

    private final LocalDate DATE = LocalDate.of(2022,1,1);
    private final LocalTime START = LocalTime.of(9,0,0);
    private final LocalTime END = LocalTime.of(10,0,0);
    private final ReservationDate SAME_RESERVATION_DATE = ReservationDate.builder()
            .date(DATE)
            .startTime(START)
            .endTime(END)
            .build();

    @BeforeEach
    void init() {
        reservationRepository = new CustomJpaQueryRepositoryImpl(new JPAQueryFactory(entityManager));
    }

    @Test
    @DisplayName("찾는 시간, 날짜에 해당하는 예약이 없으면 0을 반환")
    void findReservationByReservationDateBetween1() {
        //given
        //when
        List<Reservation> result = reservationRepository.findReservationBetween(ID1, DATE, START, END);

        //then
        Assertions.assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("찾는 날짜/시간이 같은 예약이 있다면 개수만큼 반환")
    void findReservationByReservationDateBetween2() {
        //given
        for (int i=0; i<3; i++) {
            Reservation reservation = Reservation.builder()
                    .reservationDate(SAME_RESERVATION_DATE)
                    .meetingId(ID1)
                    .build();

            entityManager.persist(reservation);
        }

        //when
        List<Reservation> result = reservationRepository.findReservationBetween(ID1, DATE, START, END);

        //then
        Assertions.assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("찾는 날짜가 같고 시작 시간이 신청 시간(시작~끝)과 겹치는 예약이 있다면 개수만큼 반환")
    void findReservationByReservationDateBetween3() {
        //given
        for (int i=0; i<3; i++) {
            ReservationDate reservationDate = ReservationDate.builder()
                    .date(DATE)
                    .startTime(START.plus(30, MINUTES))
                    .endTime(END.plus(30, MINUTES))
                    .build();

            Reservation reservation = Reservation.builder()
                    .reservationDate(reservationDate)
                    .meetingId(ID1)
                    .build();

            entityManager.persist(reservation);
        }

        //when
        List<Reservation> result = reservationRepository.findReservationBetween(ID1, DATE, START, END);

        //then
        Assertions.assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("찾는 날짜가 같고 끝나는 시간이 신청 시간(시작~끝)과 겹치는 예약이 있다면 개수만큼 반환")
    void findReservationByReservationDateBetween4() {
        //given
        for (int i=0; i<3; i++) {
            ReservationDate reservationDate = ReservationDate.builder()
                    .date(DATE)
                    .startTime(START.minus(30, MINUTES))
                    .endTime(END.minus(30, MINUTES))
                    .build();

            Reservation reservation = Reservation.builder()
                    .reservationDate(reservationDate)
                    .meetingId(ID1)
                    .build();

            entityManager.persist(reservation);
        }

        //when
        List<Reservation> result = reservationRepository.findReservationBetween(ID1, DATE, START, END);

        //then
        Assertions.assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("찾는 날짜가 같고 끝나는 시간이 신청 시간(시작)과 겹치는 예약이 있다면 반환하지 않는다.")
    void findReservationByReservationDateBetween5() {
        //given
        for (int i=0; i<3; i++) {
            ReservationDate reservationDate = ReservationDate.builder()
                    .date(DATE)
                    .startTime(START.minus(60, MINUTES))
                    .endTime(START)
                    .build();

            Reservation reservation = Reservation.builder()
                    .reservationDate(reservationDate)
                    .meetingId(ID1)
                    .build();

            entityManager.persist(reservation);
        }

        //when
        List<Reservation> result = reservationRepository.findReservationBetween(ID1, DATE, START, END);

        //then
        Assertions.assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("찾는 날짜가 같고 시작 시간이 신청 시간(끝)과 겹치는 예약이 있다면 반환하지 않는다.")
    void findReservationByReservationDateBetween6() {
        //given
        for (int i=0; i<3; i++) {
            ReservationDate reservationDate = ReservationDate.builder()
                    .date(DATE)
                    .startTime(END)
                    .endTime(END.plus(60, MINUTES))
                    .build();

            Reservation reservation = Reservation.builder()
                    .reservationDate(reservationDate)
                    .meetingId(ID1)
                    .build();

            entityManager.persist(reservation);
        }

        //when
        List<Reservation> result = reservationRepository.findReservationBetween(ID1, DATE, START, END);

        //then
        Assertions.assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("찾는 id가 다르다면 시간이 같아도 반환하지 않는다.")
    void findReservationByReservationDateBetween7() {
        //given
        for (int i=0; i<3; i++) {
            ReservationDate reservationDate = ReservationDate.builder()
                    .date(DATE)
                    .startTime(START)
                    .endTime(END.minus(30, MINUTES))
                    .build();

            Reservation reservation = Reservation.builder()
                    .reservationDate(reservationDate)
                    .meetingId(ID2)
                    .build();

            entityManager.persist(reservation);
        }

        //when
        List<Reservation> result = reservationRepository.findReservationBetween(ID1, DATE, START, END);

        //then
        Assertions.assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("찾는 날짜가 다르다면 시간이 같아도 반환하지 않는다.")
    void findReservationByReservationDateBetween8() {
        //given
        for (int i=0; i<3; i++) {
            ReservationDate reservationDate = ReservationDate.builder()
                    .date(DATE.plus(1, DAYS))
                    .startTime(START)
                    .endTime(END.minus(30, MINUTES))
                    .build();

            Reservation reservation = Reservation.builder()
                    .reservationDate(reservationDate)
                    .meetingId(ID1)
                    .build();

            entityManager.persist(reservation);
        }

        //when
        List<Reservation> result = reservationRepository.findReservationBetween(ID1, DATE, START, END);

        //then
        Assertions.assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("7번, 8번 테스트가 날짜가 같고 meetingId가 같다면 값을 반환하는지 확인한다")
    void findReservationByReservationDateBetween9() {
        //given
        for (int i=0; i<3; i++) {
            ReservationDate reservationDate = ReservationDate.builder()
                    .date(DATE)
                    .startTime(START)
                    .endTime(END.minus(30, MINUTES))
                    .build();

            Reservation reservation = Reservation.builder()
                    .reservationDate(reservationDate)
                    .meetingId(ID1)
                    .build();

            entityManager.persist(reservation);
        }

        //when
        List<Reservation> result = reservationRepository.findReservationBetween(ID1, DATE, START, END);

        //then
        Assertions.assertThat(result.size()).isEqualTo(3);
    }
}