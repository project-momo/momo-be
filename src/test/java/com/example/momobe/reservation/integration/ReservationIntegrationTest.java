package com.example.momobe.reservation.integration;

import com.example.momobe.meeting.domain.Address;
import com.example.momobe.meeting.domain.DateTime;
import com.example.momobe.meeting.domain.DateTimeInfo;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.enums.Tag;
import com.example.momobe.reservation.dto.in.RequestReservationDto;
import com.example.momobe.security.domain.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.common.enums.TestConstants.CONTENT3;
import static com.example.momobe.meeting.domain.enums.Category.AI;
import static com.example.momobe.meeting.domain.enums.DatePolicy.FREE;
import static com.example.momobe.meeting.domain.enums.DatePolicy.ONE_DAY;
import static com.example.momobe.meeting.domain.enums.MeetingState.CLOSE;
import static com.example.momobe.meeting.domain.enums.MeetingState.OPEN;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest(value = {
        "jwt.secretKey=only_test_secret_Key_value_gn..rlfdlrkqnwhrgkekspdy",
        "jwt.refreshKey=only_test_refresh_key_value_gn..rlfdlrkqnwhrgkekspdy"
})
@AutoConfigureMockMvc
public class ReservationIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EntityManager entityManager;

    Meeting meeting;
    Meeting closedMeeting;
    Meeting freeScheduledMeeting;
    Meeting freeOrderMeeting;
    String accessToken;
    String userMail;
    String userNickname;

    @BeforeEach
    void init() {
        meeting = Meeting.builder()
                .hostId(ID1)
                .category(AI)
                .title(CONTENT1)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(ONE_DAY)
                        .startDate(LocalDate.of(2022,1,1))
                        .endDate(LocalDate.of(2022,1,10))
                        .startTime(LocalTime.of(10,0,0))
                        .endTime(LocalTime.of(18,0,0))
                        .maxTime(4)
                        .dateTimes(List.of(new DateTime(LocalDateTime.of(2022,1,1,12,0,0))))
                        .build())
                .personnel(10)
                .price(10000L)
                .content(CONTENT2)
                .notice(CONTENT3)
                .tags(List.of(Tag.OFFLINE, Tag.MENTORING))
                .meetingState(OPEN)
                .address(new Address(List.of(1L,2L),"화곡동"))
                .build();

        closedMeeting = Meeting.builder()
                .hostId(ID1)
                .category(AI)
                .title(CONTENT1)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(ONE_DAY)
                        .startDate(LocalDate.of(2022,1,1))
                        .endDate(LocalDate.of(2022,1,10))
                        .startTime(LocalTime.of(10,0,0))
                        .endTime(LocalTime.of(18,0,0))
                        .maxTime(4)
                        .dateTimes(List.of(new DateTime(LocalDateTime.of(2022,1,1,12,0,0))))
                        .build())
                .personnel(10)
                .price(10000L)
                .content(CONTENT2)
                .notice(CONTENT3)
                .tags(List.of(Tag.OFFLINE, Tag.MENTORING))
                .meetingState(CLOSE)
                .address(new Address(List.of(1L,2L),"화곡동"))
                .build();

        freeScheduledMeeting = Meeting.builder()
                .hostId(ID1)
                .category(AI)
                .title(CONTENT1)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(FREE)
                        .startDate(LocalDate.of(2022,1,1))
                        .endDate(LocalDate.of(2022,1,10))
                        .startTime(LocalTime.of(10,0,0))
                        .endTime(LocalTime.of(18,0,0))
                        .maxTime(4)
                        .dateTimes(List.of(new DateTime(LocalDateTime.of(2022,1,1,12,0,0))))
                        .build())
                .personnel(1)
                .price(10000L)
                .content(CONTENT2)
                .notice(CONTENT3)
                .tags(List.of(Tag.OFFLINE, Tag.MENTORING))
                .meetingState(OPEN)
                .address(new Address(List.of(1L,2L),"화곡동"))
                .build();

        freeOrderMeeting = Meeting.builder()
                .hostId(ID1)
                .category(AI)
                .title(CONTENT1)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(FREE)
                        .startDate(LocalDate.of(2022,1,1))
                        .endDate(LocalDate.of(2022,1,10))
                        .startTime(LocalTime.of(10,0,0))
                        .endTime(LocalTime.of(18,0,0))
                        .maxTime(4)
                        .dateTimes(List.of(new DateTime(LocalDateTime.of(2022,1,1,12,0,0))))
                        .build())
                .personnel(1)
                .price(0L)
                .content(CONTENT2)
                .notice(CONTENT3)
                .tags(List.of(Tag.OFFLINE, Tag.MENTORING))
                .meetingState(OPEN)
                .address(new Address(List.of(1L,2L),"화곡동"))
                .build();

        accessToken = jwtTokenUtil.createAccessToken(EMAIL1, ID1, List.of(ROLE_USER), NICKNAME1);
        userMail = EMAIL1;
        userNickname = NICKNAME1;

        entityManager.persist(meeting);
        entityManager.persist(closedMeeting);
        entityManager.persist(freeScheduledMeeting);
        entityManager.persist(freeOrderMeeting);
    }

    @Test
    @DisplayName("정상 요청 시 201과 응답 반환")
    void saveReservationTest1() throws Exception {
        //given
        RequestReservationDto reservationDto = RequestReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(RequestReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.of(2022,1,5))
                        .startTime(LocalTime.of(10,0,0))
                        .endTime(LocalTime.of(18,0,0))
                        .build())
                .build();

        String json = objectMapper.writeValueAsString(reservationDto);

        //when
        ResultActions perform = mockMvc.perform(post("/meetings/{meetingId}/reservations", meeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        //then
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("$.payType").isString())
                .andExpect(jsonPath("$.amount").value(meeting.getPrice()))
                .andExpect(jsonPath("$.orderId").isString())
                .andExpect(jsonPath("$.orderName").value(meeting.getTitle()))
                .andExpect(jsonPath("$.customerEmail").value(userMail))
                .andExpect(jsonPath("$.customerName").value(userNickname))
                .andExpect(jsonPath("$.successUrl").isString())
                .andExpect(jsonPath("$.failUrl").isString())
                .andExpect(jsonPath("$.createDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.paySuccessYn").isString())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 meetingId 요청 시 404 반환")
    void saveReservationTest2() throws Exception {
        //given
        RequestReservationDto reservationDto = RequestReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(RequestReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.of(2022,1,5))
                        .startTime(LocalTime.of(11,0,0))
                        .endTime(LocalTime.of(12,0,0))
                        .build())
                .build();

        String json = objectMapper.writeValueAsString(reservationDto);

        //when
        ResultActions perform = mockMvc.perform(post("/meetings/{meetingId}/reservations", -1L)
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        //then
        perform.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("meeting이 closed 상태라면 409 반환")
    void saveReservationTest3() throws Exception {
        //given
        RequestReservationDto reservationDto = RequestReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(RequestReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.of(2022,1,5))
                        .startTime(LocalTime.of(11,0,0))
                        .endTime(LocalTime.of(12,0,0))
                        .build())
                .build();

        String json = objectMapper.writeValueAsString(reservationDto);

        //when
        ResultActions perform = mockMvc.perform(post("/meetings/{meetingId}/reservations", closedMeeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        //then
        perform.andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @DisplayName("free meeting이고 동시간대에 이미 예약이 존재한다면 409 반환")
    void saveReservationTest4() throws Exception {
        //given
        RequestReservationDto reservationDto = RequestReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(RequestReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.of(2022,1,5))
                        .startTime(LocalTime.of(11,0,0))
                        .endTime(LocalTime.of(12,0,0))
                        .build())
                .build();

        String json = objectMapper.writeValueAsString(reservationDto);

        //when
        mockMvc.perform(post("/meetings/{meetingId}/reservations", freeScheduledMeeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        ResultActions perform = mockMvc.perform(post("/meetings/{meetingId}/reservations", freeScheduledMeeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        //then
        perform.andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @DisplayName("요청 금액과 실제 서버에서 계산한 금액이 일치하지 않을 경우 409 반환")
    void saveReservationTest5() throws Exception {
        //given
        RequestReservationDto reservationDto = RequestReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(RequestReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.of(2022,1,5))
                        .startTime(LocalTime.of(11,0,0))
                        .endTime(LocalTime.of(13,0,0))
                        .build())
                .build();

        String json = objectMapper.writeValueAsString(reservationDto);

        //when
        mockMvc.perform(post("/meetings/{meetingId}/reservations", freeScheduledMeeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        ResultActions perform = mockMvc.perform(post("/meetings/{meetingId}/reservations", freeScheduledMeeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        //then
        perform.andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @DisplayName("meeting에 정의된 시간이 아닐 경우 409 발생")
    void saveReservationTest6() throws Exception {
        //given
        RequestReservationDto reservationDto = RequestReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(RequestReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.of(2022,2,5))
                        .startTime(LocalTime.of(11,0,0))
                        .endTime(LocalTime.of(12,0,0))
                        .build())
                .build();

        String json = objectMapper.writeValueAsString(reservationDto);

        //when
        mockMvc.perform(post("/meetings/{meetingId}/reservations", freeScheduledMeeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        ResultActions perform = mockMvc.perform(post("/meetings/{meetingId}/reservations", freeScheduledMeeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        //then
        perform.andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @DisplayName("meeting에 정의된 최대 연속 예약 시간을 넘어설 경우 409 발생")
    void saveReservationTest7() throws Exception {
        //given
        RequestReservationDto reservationDto = RequestReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(RequestReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.of(2022,2,5))
                        .startTime(LocalTime.of(11,0,0))
                        .endTime(LocalTime.of(18,0,0))
                        .build())
                .build();

        String json = objectMapper.writeValueAsString(reservationDto);

        //when
        mockMvc.perform(post("/meetings/{meetingId}/reservations", freeScheduledMeeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        ResultActions perform = mockMvc.perform(post("/meetings/{meetingId}/reservations", freeScheduledMeeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        //then
        perform.andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @DisplayName("무료 모임일 경우 orderName, customerEmail, customerName, createDate, amount 외에는 Null 반환")
    void saveReservationTest8() throws Exception {
        //given
        RequestReservationDto reservationDto = RequestReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(0L)
                .dateInfo(RequestReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.of(2022,1,5))
                        .startTime(LocalTime.of(11,0,0))
                        .endTime(LocalTime.of(12,0,0))
                        .build())
                .build();

        String json = objectMapper.writeValueAsString(reservationDto);

        //when
        ResultActions perform = mockMvc.perform(post("/meetings/{meetingId}/reservations", freeOrderMeeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        //then
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("$.payType").isEmpty())
                .andExpect(jsonPath("$.amount").value(0))
                .andExpect(jsonPath("$.orderId").isEmpty())
                .andExpect(jsonPath("$.orderName").value(meeting.getTitle()))
                .andExpect(jsonPath("$.customerEmail").value(userMail))
                .andExpect(jsonPath("$.customerName").value(userNickname))
                .andExpect(jsonPath("$.successUrl").isEmpty())
                .andExpect(jsonPath("$.failUrl").isEmpty())
                .andExpect(jsonPath("$.createDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.paySuccessYn").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("date policy가 free가 아닐 경우 start time과 end time이 meeting과 일치해야함")
    void saveReservationTest9() throws Exception {
        //given
        RequestReservationDto reservationDto = RequestReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(RequestReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.of(2022,1,5))
                        .startTime(LocalTime.of(11,0,0))
                        .endTime(LocalTime.of(13,0,0))
                        .build())
                .build();

        String json = objectMapper.writeValueAsString(reservationDto);

        //when
        ResultActions perform = mockMvc.perform(post("/meetings/{meetingId}/reservations", meeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        //then
        perform.andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @DisplayName("정원이 가득찼을 경우 409 반환")
    void saveReservationTest10() throws Exception {
        //given
        RequestReservationDto reservationDto = RequestReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(RequestReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.of(2022,1,5))
                        .startTime(LocalTime.of(10,0,0))
                        .endTime(LocalTime.of(18,0,0))
                        .build())
                .build();

        String json = objectMapper.writeValueAsString(reservationDto);

        //when
        for (int i=0; i<10; i++) {
            mockMvc.perform(post("/meetings/{meetingId}/reservations", meeting.getId())
                    .contentType(APPLICATION_JSON)
                    .content(json)
                    .header(JWT_HEADER, accessToken));
        }

        ResultActions perform = mockMvc.perform(post("/meetings/{meetingId}/reservations", meeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        //then
        perform.andExpect(status().isConflict())
                .andDo(print());
    }
}
