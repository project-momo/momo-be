package com.example.momobe.user.domain;

import com.example.momobe.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PointTest {

    @Test
    @DisplayName("plus() 메서드를 실행하면 해당 값만큼 포인트가 증가한 Point 객체를 반환한다.")
    void plus() {
        //given
        Point point = new Point(100L);

        //when
        Point result = point.plus(100L);

        //then
        assertThat(result.getPoint()).isEqualTo(200L);
    }

    @Test
    @DisplayName("minus() 메서드를 실행하면 해당 값만큼 포인트가 감소한 Point 객체를 반환한다.")
    void minus() {
        //given
        Point point = new Point(100L);

        //when
        Point result = point.minus(100L);

        //then
        assertThat(result.getPoint()).isEqualTo(0L);
    }

    @Test
    @DisplayName("minus() 메서드 실행 시 포인트가 음수라면 예외 발생")
    void minus_exception() {
        //given
        Point point = new Point(100L);

        //when //then
        assertThatThrownBy(() -> point.minus(200L))
                .isInstanceOf(CustomException.class);
    }
}