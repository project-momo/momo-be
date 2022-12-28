package com.example.momobe.meeting.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.momobe.common.util.ValidatorUtil.getArray;
import static org.assertj.core.api.Assertions.assertThat;

public class MeetingRequestDtoTest {
    private final String TITLE = "title";
    private final String CONTENT = "content";
    private final String TAGS = "tags";

    @Test
    @DisplayName("MeetingRequestDto의 title이 공백이면 예외가 발생한다.")
    public void title_failed_1() throws Exception {
        // given
        MeetingRequestDto requestDto = MeetingRequestDto.builder().title(" ").build();

        // when
        Object[] arr = getArray(requestDto, TITLE);

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("MeetingRequestDto의 title이 null이면 예외가 발생한다.")
    public void title_failed_2() throws Exception {
        // given
        MeetingRequestDto requestDto = MeetingRequestDto.builder().build();

        // when
        Object[] arr = getArray(requestDto, TITLE);

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("MeetingRequestDto의 content가 공백이면 예외가 발생한다.")
    public void content_failed_1() throws Exception {
        // given
        MeetingRequestDto requestDto = MeetingRequestDto.builder().content(" ").build();

        // when
        Object[] arr = getArray(requestDto, CONTENT);

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("MeetingRequestDto의 content가 null이면 예외가 발생한다.")
    public void content_failed_2() throws Exception {
        // given
        MeetingRequestDto requestDto = MeetingRequestDto.builder().build();

        // when
        Object[] arr = getArray(requestDto, CONTENT);

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("MeetingRequestDto의 tags.size()가 1 미만이면 예외가 발생한다.")
    public void tagIds_failed_1() throws Exception {
        // given
        MeetingRequestDto requestDto1 = MeetingRequestDto.builder().tags(List.of()).build();

        // when
        Object[] arr = getArray(requestDto1, TAGS);

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("MeetingRequestDto의 tags가 null이면 예외가 발생한다.")
    public void tagIds_failed_2() throws Exception {
        // given
        MeetingRequestDto requestDto = MeetingRequestDto.builder().build();

        // when
        Object[] arr = getArray(requestDto, TAGS);

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("MeetingRequestDto의 address가 null이면 예외가 발생한다.")
    public void address_failed() throws Exception {
        // given
        MeetingRequestDto requestDto = MeetingRequestDto.builder().build();

        // when
        Object[] arr = getArray(requestDto, "address");

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("MeetingRequestDto의 dateTime이 null이면 예외가 발생한다.")
    public void dateTimes_failed() throws Exception {
        // given
        MeetingRequestDto requestDto =
                MeetingRequestDto.builder().build();

        // when
        Object[] arr = getArray(requestDto, "dateTime");

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("MeetingRequestDto의 personnel이 1보다 작으면 예외가 발생한다.")
    public void personnel_failed() throws Exception {
        // given
        MeetingRequestDto requestDto = MeetingRequestDto.builder().personnel(0).build();

        // when
        Object[] arr = getArray(requestDto, "personnel");

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("MeetingRequestDto의 price가 null이면 예외가 발생한다.")
    public void priceInfo_failed_1() throws Exception {
        // given
        MeetingRequestDto requestDto = MeetingRequestDto.builder().build();

        // when
        Object[] arr = getArray(requestDto, "price");

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("MeetingRequestDto의 price가 0미만이면 예외가 발생한다.")
    public void priceInfo_failed_2() throws Exception {
        // given
        MeetingRequestDto requestDto = MeetingRequestDto.builder().price(-1L).build();

        // when
        Object[] arr = getArray(requestDto, "price");

        // then
        assertThat(arr).isNotEmpty();
    }

}
