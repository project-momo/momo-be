package com.example.momobe.reservation.dto.in;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PatchReservationDtoTest {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("true, false가 아닌 문자열이 입력된 경우 예외 발생")
    void getIsAccepted1() {
        //given
        PatchReservationDto dto = new PatchReservationDto("truw");

        //when
        Set<ConstraintViolation<PatchReservationDto>> result = validator.validate(dto);

        //then
        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("true, false가 아닌 문자열이 입력된 경우 예외 발생")
    void getIsAccepted2() {
        //given
        PatchReservationDto dto = new PatchReservationDto("falsq");

        //when
        Set<ConstraintViolation<PatchReservationDto>> result = validator.validate(dto);

        //then
        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("true 혹은 false가 입력될 경우 예외가 발생하지 않는다")
    void getIsAccepted3() {
        //given
        PatchReservationDto dto = new PatchReservationDto("true");

        //when
        Set<ConstraintViolation<PatchReservationDto>> result = validator.validate(dto);

        //then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("true 혹은 false가 입력될 경우 예외가 발생하지 않는다")
    void getIsAccepted4() {
        //given
        PatchReservationDto dto = new PatchReservationDto("false");

        //when
        Set<ConstraintViolation<PatchReservationDto>> result = validator.validate(dto);

        //then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("대, 소문자 관계 없이 true 혹은 false일 경우 예외를 반환하지 않는다")
    void getIsAccepted5() {
        //given
        PatchReservationDto dto = new PatchReservationDto("faLSE");

        //when
        Set<ConstraintViolation<PatchReservationDto>> result = validator.validate(dto);

        //then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("true 혹은 false가 입력될 경우 예외가 발생하지 않는다")
    void getIsAccepted6() {
        //given
        PatchReservationDto dto = new PatchReservationDto("TRUE");

        //when
        Set<ConstraintViolation<PatchReservationDto>> result = validator.validate(dto);

        //then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("isAccepted가 null일 경우 예외 발생")
    void getIsAccepted7() {
        //given
        PatchReservationDto dto = new PatchReservationDto(null);

        //when
        Set<ConstraintViolation<PatchReservationDto>> result = validator.validate(dto);

        //then
        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("isAccepted가 empty일 경우 예외 발생")
    void getIsAccepted8() {
        //given
        PatchReservationDto dto = new PatchReservationDto("");

        //when
        Set<ConstraintViolation<PatchReservationDto>> result = validator.validate(dto);

        //then
        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("isAccepted가 blank일 경우 예외 발생")
    void getIsAccepted9() {
        //given
        PatchReservationDto dto = new PatchReservationDto("             ");

        //when
        Set<ConstraintViolation<PatchReservationDto>> result = validator.validate(dto);

        //then
        Assertions.assertThat(result).isNotEmpty();
    }
}