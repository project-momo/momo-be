package com.example.momobe.reservation.dto.in;

import com.example.momobe.common.enums.TestConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.assertj.core.api.Assertions.*;

class RequestReservationDtoTest {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("dateInfo가 null일 경우 예외 발생")
    void test1() {
        //given
        RequestReservationDto dto = RequestReservationDto.builder()
                .dateInfo(null)
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .build();

        //when
        Set<ConstraintViolation<RequestReservationDto>> result = validator.validate(dto);

        //then
        assertThat(result.size()).isNotZero();
    }

    @Test
    @DisplayName("dateInfo의 reservationDate가 null일 경우 예외 발생")
    void test2() {
        //given
        RequestReservationDto dto = RequestReservationDto.builder()
                .dateInfo(RequestReservationDto.ReservationDateDto.builder()
                        .reservationDate(null)
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now())
                        .build())
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .build();

        //when
        Set<ConstraintViolation<RequestReservationDto>> result = validator.validate(dto);

        //then
        assertThat(result.size()).isNotZero();
    }

    @Test
    @DisplayName("dateInfo의 startTime이 null일 경우 예외 발생")
    void test3() {
        //given
        RequestReservationDto dto = RequestReservationDto.builder()
                .dateInfo(RequestReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.now())
                        .startTime(null)
                        .endTime(LocalTime.now())
                        .build())
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .build();

        //when
        Set<ConstraintViolation<RequestReservationDto>> result = validator.validate(dto);

        //then
        assertThat(result.size()).isNotZero();
    }

    @Test
    @DisplayName("dateInfo의 endTime이 null일 경우 예외 발생")
    void test4() {
        //given
        RequestReservationDto dto = RequestReservationDto.builder()
                .dateInfo(RequestReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.now())
                        .startTime(LocalTime.now())
                        .endTime(null)
                        .build())
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .build();

        //when
        Set<ConstraintViolation<RequestReservationDto>> result = validator.validate(dto);

        //then
        assertThat(result.size()).isNotZero();
    }

    @Test
    @DisplayName("amount가 null일 경우 예외 발생")
    void test5() {
        //given
        RequestReservationDto dto = RequestReservationDto.builder()
                .dateInfo(RequestReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.now())
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now())
                        .build())
                .reservationMemo(CONTENT1)
                .amount(null)
                .build();

        //when
        Set<ConstraintViolation<RequestReservationDto>> result = validator.validate(dto);

        //then
        assertThat(result.size()).isNotZero();
    }

    @Test
    @DisplayName("reservationMemo가 Null이여도 예외 발생하지 않음")
    void test6() {
        //given
        RequestReservationDto dto = RequestReservationDto.builder()
                .dateInfo(RequestReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.now())
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now())
                        .build())
                .reservationMemo(null)
                .amount(10000L)
                .build();

        //when
        Set<ConstraintViolation<RequestReservationDto>> result = validator.validate(dto);

        //then
        assertThat(result.size()).isZero();
    }
}