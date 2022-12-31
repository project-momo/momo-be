package com.example.momobe.meeting.enums;

import com.example.momobe.meeting.domain.Address;
import com.example.momobe.meeting.domain.DateTime;
import com.example.momobe.meeting.domain.DateTimeInfo;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.enums.*;
import com.example.momobe.meeting.dto.MeetingRequestDto;
import org.springframework.restdocs.payload.FieldDescriptor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.domain.enums.Category.SOCIAL;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class MeetingConstants {
    public static final LocalDate START_DATE = LocalDate.of(2022, 12, 25);
    public static final LocalDate END_DATE = LocalDate.of(2022, 12, 31);
    public static final LocalTime START_TIME = LocalTime.of(1, 0);
    public static final LocalTime END_TIME = LocalTime.of(5, 0);
    public static final Integer MAX_TIME = 3;
    public static final Long PRICE = 20000L;
    public static final String NOTICE = "전달 사항";

    public static Meeting generateMeeting() {
        return Meeting.builder()
                .title(TITLE1)
                .content(CONTENT1)
                .hostId(ID1)
                .category(SOCIAL)
                .meetingState(MeetingState.OPEN)
                .price(PRICE)
                .dateTimeInfo(new DateTimeInfo(DatePolicy.ONE_DAY,
                        START_DATE, END_DATE, START_TIME, END_TIME, MAX_TIME,
                        List.of(new DateTime(LocalDateTime.of(START_DATE, START_TIME)))))
                .personnel(1)
                .notice(NOTICE)
                .address(new Address(List.of(1L, 2L), "추가 주소"))
                .build();
    }

    public static Meeting generateMeeting(Long hostId) {
        return Meeting.builder()
                .title(TITLE1)
                .content(CONTENT1)
                .hostId(hostId)
                .category(SOCIAL)
                .meetingState(MeetingState.OPEN)
                .price(PRICE)
                .dateTimeInfo(new DateTimeInfo(DatePolicy.ONE_DAY,
                        START_DATE, END_DATE, START_TIME, END_TIME, MAX_TIME,
                        List.of(new DateTime(LocalDateTime.of(START_DATE, START_TIME)))))
                .personnel(1)
                .notice(NOTICE)
                .address(new Address(List.of(1L, 2L), "추가 주소"))
                .build();
    }

    public static Meeting generateMeeting(Long hostId, List<Long> addressIds) {
        return Meeting.builder()
                .title(TITLE1)
                .content(CONTENT1)
                .hostId(hostId)
                .category(SOCIAL)
                .meetingState(MeetingState.OPEN)
                .price(PRICE)
                .dateTimeInfo(new DateTimeInfo(DatePolicy.ONE_DAY,
                        START_DATE, END_DATE, START_TIME, END_TIME, MAX_TIME,
                        List.of(new DateTime(LocalDateTime.of(START_DATE, START_TIME)))))
                .personnel(1)
                .notice(NOTICE)
                .address(new Address(addressIds, "추가 주소"))
                .build();
    }

    public static final MeetingRequestDto.AddressDto ADDRESS_DTO1 = MeetingRequestDto.AddressDto.builder()
            .addressIds(List.of(1L, 2L))
            .addressInfo(SUB_ADDRESS1)
            .build();
    public static final MeetingRequestDto.AddressDto ADDRESS_DTO2 = MeetingRequestDto.AddressDto.builder()
            .addressIds(List.of(1L, 2L))
            .addressInfo(SUB_ADDRESS2)
            .build();

    public static final MeetingRequestDto.DateTimeDto DATE_TIME_DTO_WITH_ONE_DAY = MeetingRequestDto.DateTimeDto.builder()
            .datePolicy(DatePolicy.ONE_DAY)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .startTime(START_TIME)
            .endTime(END_TIME)
            .maxTime(MAX_TIME)
            .build();

    public static final MeetingRequestDto MEETING_REQUEST_DTO_WITH_ONE_DAY = MeetingRequestDto.builder()
            .category(SOCIAL)
            .title(TITLE1)
            .content(CONTENT1)
            .tags(List.of(Tag.MEETING, Tag.ONLINE, Tag.OFFLINE))
            .address(ADDRESS_DTO1)
            .dateTime(DATE_TIME_DTO_WITH_ONE_DAY)
            .personnel(3)
            .notice(NOTICE)
            .price(PRICE)
            .build();

    public static final MeetingRequestDto.DateTimeDto DATE_TIME_DTO_WITH_PERIOD = MeetingRequestDto.DateTimeDto.builder()
            .datePolicy(DatePolicy.PERIOD)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .startTime(START_TIME)
            .endTime(END_TIME)
            .maxTime(MAX_TIME)
            .dayWeeks(new LinkedHashSet<>(List.of(1, 2, 7)))
            .build();

    public static final MeetingRequestDto MEETING_REQUEST_DTO_WITH_PERIOD = MeetingRequestDto.builder()
            .category(SOCIAL)
            .title(TITLE1)
            .content(CONTENT1)
            .tags(List.of(Tag.MEETING, Tag.ONLINE, Tag.OFFLINE))
            .address(ADDRESS_DTO1)
            .dateTime(DATE_TIME_DTO_WITH_PERIOD)
            .personnel(3)
            .notice(NOTICE)
            .price(PRICE)
            .build();

    public static final MeetingRequestDto.DateTimeDto DATE_TIME_DTO_WITH_FREE = MeetingRequestDto.DateTimeDto.builder()
            .datePolicy(DatePolicy.FREE)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .startTime(START_TIME)
            .endTime(END_TIME)
            .maxTime(MAX_TIME)
            .dates(List.of(LocalDate.now(), LocalDate.now().plusDays(2),
                    LocalDate.now().plusDays(5), LocalDate.now().plusDays(10)))
            .build();

    public static final MeetingRequestDto MEETING_REQUEST_DTO_WITH_FREE = MeetingRequestDto.builder()
            .category(SOCIAL)
            .title(TITLE1)
            .content(CONTENT1)
            .tags(List.of(Tag.MEETING, Tag.ONLINE, Tag.OFFLINE))
            .address(ADDRESS_DTO1)
            .dateTime(DATE_TIME_DTO_WITH_FREE)
            .personnel(1)
            .notice(NOTICE)
            .price(PRICE)
            .build();

    public static final MeetingRequestDto.DateTimeDto DATE_TIME_DTO_WITH_ALL = MeetingRequestDto.DateTimeDto.builder()
            .datePolicy(DatePolicy.FREE)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(10))
            .startTime(START_TIME)
            .endTime(END_TIME)
            .maxTime(MAX_TIME)
            .dayWeeks(new LinkedHashSet<>(List.of(1, 2, 7)))
            .dates(List.of(LocalDate.now(), LocalDate.now().plusDays(2),
                    LocalDate.now().plusDays(5), LocalDate.now().plusDays(10)))
            .build();

    public static final MeetingRequestDto MEETING_REQUEST_DTO_WITH_ALL = MeetingRequestDto.builder()
            .category(SOCIAL)
            .title(TITLE1)
            .content(CONTENT1)
            .tags(List.of(Tag.MEETING, Tag.ONLINE, Tag.OFFLINE))
            .address(ADDRESS_DTO1)
            .dateTime(DATE_TIME_DTO_WITH_ALL)
            .personnel(1)
            .notice(NOTICE)
            .price(PRICE)
            .build();

    public static final FieldDescriptor FWP_CONTENT_MEETING_ID = fieldWithPath("content[].meetingId").type(NUMBER).description("모임 식별자");
    public static final FieldDescriptor FWP_CONTENT_CATEGORY = fieldWithPath("content[].category").type(STRING).description("카테고리");
    public static final FieldDescriptor FWP_CONTENT_HOST = fieldWithPath("content[].host").type(OBJECT).description("주최자 정보");
    public static final FieldDescriptor FWP_CONTENT_HOST_USER_ID = fieldWithPath("content[].host.userId").type(NUMBER).description("주최자 식별자");
    public static final FieldDescriptor FWP_CONTENT_HOST_NICKNAME = fieldWithPath("content[].host.nickname").type(STRING).description("주최자 닉네임");
    public static final FieldDescriptor FWP_CONTENT_HOST_IMAGE_URL = fieldWithPath("content[].host.imageUrl").type(STRING).description("주최자 이미지");
    public static final FieldDescriptor FWP_CONTENT_TITLE = fieldWithPath("content[].title").type(STRING).description("제목");
    public static final FieldDescriptor FWP_CONTENT_CONTENT = fieldWithPath("content[].content").type(STRING).description("내용");
    public static final FieldDescriptor FWP_CONTENT_ADDRESS = fieldWithPath("content[].address").type(OBJECT).description("주소 정보");
    public static final FieldDescriptor FWP_CONTENT_ADDRESS_ADDRESSES = fieldWithPath("content[].address.addresses").type(ARRAY).description("주소");
    public static final FieldDescriptor FWP_CONTENT_ADDRESS_ADDRESS_INFO = fieldWithPath("content[].address.addressInfo").type(STRING).description("추가 주소");
    public static final FieldDescriptor FWP_CONTENT_MEETING_STATE = fieldWithPath("content[].meetingState").type(STRING).description("모임 상태");
    public static final FieldDescriptor FWP_CONTENT_IS_OPEN = fieldWithPath("content[].isOpen").type(BOOLEAN).description("모임 오픈 여부");
    public static final FieldDescriptor FWP_CONTENT_DATE_TIME = fieldWithPath("content[].dateTime").type(OBJECT).description("날짜 정보");
    public static final FieldDescriptor FWP_CONTENT_DATE_TIME_DATE_POLICY = fieldWithPath("content[].dateTime.datePolicy").type(STRING).description("날짜 정책");
    public static final FieldDescriptor FWP_CONTENT_DATE_TIME_START_DATE = fieldWithPath("content[].dateTime.startDate").type(STRING).description("시작 날짜");
    public static final FieldDescriptor FWP_CONTENT_DATE_TIME_END_DATE = fieldWithPath("content[].dateTime.endDate").type(STRING).description("끝나는 날짜");
    public static final FieldDescriptor FWP_CONTENT_DATE_TIME_START_TIME = fieldWithPath("content[].dateTime.startTime").type(STRING).description("시작 시간");
    public static final FieldDescriptor FWP_CONTENT_DATE_TIME_END_TIME = fieldWithPath("content[].dateTime.endTime").type(STRING).description("끝나는 시간");
    public static final FieldDescriptor FWP_CONTENT_DATE_TIME_MAX_TIME = fieldWithPath("content[].dateTime.maxTime").type(NUMBER).description("최대 예약 가능 시간");
    public static final FieldDescriptor FWP_CONTENT_DATE_TIME_DAY_WEEKS = fieldWithPath("content[].dateTime.dayWeeks").type(ARRAY).description("요일 (월: 1 ~ 일: 7, datePolicy가 PEROID일 때만)");
    public static final FieldDescriptor FWP_CONTENT_DATE_TIME_DATES = fieldWithPath("content[].dateTime.dates").type(ARRAY).description("날짜");
    public static final FieldDescriptor FWP_CONTENT_PRICE = fieldWithPath("content[].price").type(NUMBER).description("가격");
}
