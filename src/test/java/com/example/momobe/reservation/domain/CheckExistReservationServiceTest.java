package com.example.momobe.reservation.domain;

import com.example.momobe.reservation.domain.enums.ReservationState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CheckExistReservationServiceTest {
    @InjectMocks
    CheckExistReservationService checkExistReservationService;

    @Mock
    CustomReservationRepository customReservationRepository;

    private final LocalDate DATE = LocalDate.of(2022,1,1);
    private final LocalTime START = LocalTime.of(9,0,0);
    private final LocalTime END = LocalTime.of(10,0,0);

    @Test
    @DisplayName("countOf 메서드 실행 시 customReservationRepository가 1회 호출된다")
    void countOfTest1() {
        //given
        Reservation reservation = Reservation.builder()
                .reservationState(ReservationState.CANCEL)
                .build();
        BDDMockito.given(customReservationRepository.findReservationBetween(ID1, DATE, START, END)).willReturn(List.of(reservation));

        //when
        checkExistReservationService.countOf(ID1, DATE, START, END);

        //then
        verify(customReservationRepository, times(1)).findReservationBetween(ID1, DATE, START, END);
    }

    @Test
    @DisplayName("countOf 메서드 실행 시 reservationState가 Canceled가 아닌 개수만큼 Long을 반환한다.")
    void countOfTest2() {
        //given
        Reservation canceledReservation = Reservation.builder()
                .reservationState(ReservationState.CANCEL)
                .build();
        Reservation reservation = Reservation.builder()
                .reservationState(ReservationState.ACCEPT)
                .build();

        List<Reservation> reservations = List.of(canceledReservation, canceledReservation, canceledReservation,
                reservation, reservation, reservation);

        BDDMockito.given(customReservationRepository.findReservationBetween(ID1, DATE, START, END)).willReturn(reservations);

        //when
        Long result = checkExistReservationService.countOf(ID1, DATE, START, END);

        //then
        Assertions.assertThat(result).isEqualTo(3L);
    }
}