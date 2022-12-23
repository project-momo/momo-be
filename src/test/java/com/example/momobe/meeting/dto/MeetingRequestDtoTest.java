package com.example.momobe.meeting.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static com.example.momobe.common.util.ValidatorUtil.getArray;
import static org.assertj.core.api.Assertions.assertThat;

public class MeetingRequestDtoTest {
    private final String TITLE = "title";
    private final String CONTENT = "content";
    private final String TAG_IDS = "tagIds";

    @Test
    @DisplayName("MeetingRequestDto의 categoryId가 0이하면 예외가 발생한다.")
    public void categoryId_failed() throws Exception {
        // given
        MeetingRequestDto requestDto1 = MeetingRequestDto.builder().categoryId(0L).build();
        MeetingRequestDto requestDto2 = MeetingRequestDto.builder().categoryId(-1L).build();

        // when
        String CATEGORY_ID = "categoryId";
        Object[] arr1 = getArray(requestDto1, CATEGORY_ID);
        Object[] arr2 = getArray(requestDto2, CATEGORY_ID);

        // then
        assertThat(arr1).isNotEmpty();
        assertThat(arr2).isNotEmpty();
    }

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
    @DisplayName("MeetingRequestDto의 tagIds.size()가 1 미만이면 예외가 발생한다.")
    public void tagIds_failed_1() throws Exception {
        // given
        MeetingRequestDto requestDto1 = MeetingRequestDto.builder().tagIds(Set.of()).build();

        // when
        Object[] arr = getArray(requestDto1, TAG_IDS);

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("MeetingRequestDto의 tagIds가 null이면 예외가 발생한다.")
    public void tagIds_failed_2() throws Exception {
        // given
        MeetingRequestDto requestDto = MeetingRequestDto.builder().build();

        // when
        Object[] arr = getArray(requestDto, TAG_IDS);

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("MeetingRequestDto의 locations.size()가 1 미만이거나 3 초과면 예외가 발생한다.")
    public void locations_failed_1() throws Exception {
        MeetingRequestDto.LocationDto locationDto = MeetingRequestDto.LocationDto.builder().build();
        // given
        MeetingRequestDto requestDto1 = MeetingRequestDto.builder().build();
        MeetingRequestDto requestDto2 = MeetingRequestDto.builder()
                .locations(List.of(locationDto, locationDto, locationDto, locationDto)).build();

        // when
        Object[] arr1 = getArray(requestDto1, "locations");
        Object[] arr2 = getArray(requestDto2, "locations");

        // then
        assertThat(arr1).isNotEmpty();
        assertThat(arr2).isNotEmpty();
    }

    @Test
    @DisplayName("MeetingRequestDto의 locations가 null이면 예외가 발생한다.")
    public void locations_failed_2() throws Exception {
        // given
        MeetingRequestDto requestDto = MeetingRequestDto.builder().build();

        // when
        Object[] arr = getArray(requestDto, "locations");

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("MeetingRequestDto의 priceInfo가 null이면 예외가 발생한다.")
    public void priceInfo_failed() throws Exception {
        // given
        MeetingRequestDto requestDto = MeetingRequestDto.builder().build();

        // when
        Object[] arr = getArray(requestDto, "priceInfo");

        // then
        assertThat(arr).isNotEmpty();
    }

}
