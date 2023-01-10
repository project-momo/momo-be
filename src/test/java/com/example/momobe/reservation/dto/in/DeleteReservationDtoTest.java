package com.example.momobe.reservation.dto.in;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static com.example.momobe.common.enums.TestConstants.*;

class DeleteReservationDtoTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeEach
    void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    @DisplayName("paymentKey가 null이면 예외 발생")
    void test1() {
        //given
        DeleteReservationDto dto = DeleteReservationDto.builder()
                .paymentKey(null)
                .cancelReason(CONTENT1)
                .build();

        //when
        Set<ConstraintViolation<DeleteReservationDto>> validate = validator.validate(dto);

        //then
        Assertions.assertThat(validate).isNotEmpty();
    }

    @Test
    @DisplayName("paymentKey가 empty면 예외 발생")
    void test2() {
        //given
        DeleteReservationDto dto = DeleteReservationDto.builder()
                .paymentKey("")
                .cancelReason(CONTENT1)
                .build();

        //when
        Set<ConstraintViolation<DeleteReservationDto>> validate = validator.validate(dto);

        //then
        Assertions.assertThat(validate).isNotEmpty();
    }

    @Test
    @DisplayName("paymentKey가 white space면 예외 발생")
    void test3() {
        //given
        DeleteReservationDto dto = DeleteReservationDto.builder()
                .paymentKey("    ")
                .cancelReason(CONTENT1)
                .build();

        //when
        Set<ConstraintViolation<DeleteReservationDto>> validate = validator.validate(dto);

        //then
        Assertions.assertThat(validate).isNotEmpty();
    }

    @Test
    @DisplayName("cancelReason이 null이면 예외 발생")
    void test4() {
        //given
        DeleteReservationDto dto = DeleteReservationDto.builder()
                .paymentKey(CONTENT1)
                .cancelReason(null)
                .build();

        //when
        Set<ConstraintViolation<DeleteReservationDto>> validate = validator.validate(dto);

        //then
        Assertions.assertThat(validate).isNotEmpty();
    }

    @Test
    @DisplayName("cancelReason이 white space면 예외 발생")
    void test5() {
        //given
        DeleteReservationDto dto = DeleteReservationDto.builder()
                .paymentKey(CONTENT1)
                .cancelReason("    ")
                .build();

        //when
        Set<ConstraintViolation<DeleteReservationDto>> validate = validator.validate(dto);

        //then
        Assertions.assertThat(validate).isNotEmpty();
    }

    @Test
    @DisplayName("cancelReason이 empty면 예외 발생")
    void test6() {
        //given
        DeleteReservationDto dto = DeleteReservationDto.builder()
                .paymentKey(CONTENT1)
                .cancelReason("")
                .build();

        //when
        Set<ConstraintViolation<DeleteReservationDto>> validate = validator.validate(dto);

        //then
        Assertions.assertThat(validate).isNotEmpty();
    }

    @Test
    @DisplayName("cancelReason과 paymentKey가 모두 not Blank라면 예외 발생하지 않음")
    void test7() {
        //given
        DeleteReservationDto dto = DeleteReservationDto.builder()
                .paymentKey(CONTENT1)
                .cancelReason(CONTENT1)
                .build();

        //when
        Set<ConstraintViolation<DeleteReservationDto>> validate = validator.validate(dto);

        //then
        Assertions.assertThat(validate).isEmpty();
    }
}