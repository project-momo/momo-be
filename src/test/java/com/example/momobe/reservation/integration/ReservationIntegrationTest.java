package com.example.momobe.reservation.integration;

import com.example.momobe.meeting.domain.Address;
import com.example.momobe.meeting.domain.DateTime;
import com.example.momobe.meeting.domain.DateTimeInfo;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.reservation.domain.Money;
import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.reservation.domain.ReservationDate;
import com.example.momobe.reservation.domain.ReservedUser;
import com.example.momobe.reservation.domain.enums.ReservationState;
import com.example.momobe.reservation.dto.in.PatchReservationDto;
import com.example.momobe.reservation.dto.in.PostReservationDto;
import com.example.momobe.reservation.event.ReservationConfirmedEvent;
import com.example.momobe.security.domain.JwtTokenUtil;
import com.example.momobe.user.domain.Email;
import com.example.momobe.user.domain.Nickname;
import com.example.momobe.user.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEvent;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.domain.enums.Category.AI;
import static com.example.momobe.meeting.domain.enums.DatePolicy.FREE;
import static com.example.momobe.meeting.domain.enums.DatePolicy.ONE_DAY;
import static com.example.momobe.meeting.domain.enums.MeetingState.CLOSE;
import static com.example.momobe.meeting.domain.enums.MeetingState.OPEN;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest(value = {
        "jwt.secretKey=only_test_secret_Key_value_gn..rlfdlrkqnwhrgkekspdy",
        "jwt.refreshKey=only_test_refresh_key_value_gn..rlfdlrkqnwhrgkekspdy"
})
@RecordApplicationEvents
@AutoConfigureMockMvc
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
public class ReservationIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EntityManager entityManager;

    @Autowired
    EntityManager em;

    @Autowired
    ApplicationEvents applicationEvents;

    private Meeting meeting;
    private Meeting closedMeeting;
    private Meeting freeScheduledMeeting;
    private Meeting freeOrderMeeting;
    private String accessToken;
    private String userMail;
    private String userNickname;
    private Reservation reservation;
    private String hostToken;

    @BeforeEach
    void init() {
        User user = User.builder()
                .email(new Email(EMAIL2))
                .build();
        User host = User.builder().build();
        em.persist(user);
        em.persist(host);
        hostToken = jwtTokenUtil.createAccessToken(EMAIL1, host.getId(), ROLE_USER_LIST, NICKNAME1);
        accessToken = jwtTokenUtil.createAccessToken(EMAIL1, user.getId(), ROLE_USER_LIST, NICKNAME1);
        userMail = EMAIL1;
        userNickname = NICKNAME1;

        meeting = Meeting.builder()
                .hostId(host.getId())
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
                .tagIds(List.of(ID1, ID2))
                .meetingState(OPEN)
                .address(new Address(List.of(1L,2L),"화곡동"))
                .build();

        closedMeeting = Meeting.builder()
                .hostId(host.getId())
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
                .tagIds(List.of(ID1, ID2))
                .meetingState(CLOSE)
                .address(new Address(List.of(1L,2L),"화곡동"))
                .build();

        freeScheduledMeeting = Meeting.builder()
                .hostId(host.getId())
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
                .tagIds(List.of(ID1, ID2))
                .meetingState(OPEN)
                .address(new Address(List.of(1L,2L),"화곡동"))
                .build();

        freeOrderMeeting = Meeting.builder()
                .hostId(host.getId())
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
                .tagIds(List.of(ID1, ID2))
                .meetingState(OPEN)
                .address(new Address(List.of(1L,2L),"화곡동"))
                .build();

        reservation = Reservation.builder()
                .amount(new Money(0L))
                .meetingId(meeting.getId())
                .reservationDate(ReservationDate.builder()
                        .date(LocalDate.now().plus(1, ChronoUnit.MONTHS))
                        .startTime(LocalTime.of(10,0))
                        .endTime(LocalTime.of(22,0))
                        .build())
                .reservedUser(new ReservedUser(user.getId()))
                .build();

        entityManager.persist(meeting);
        entityManager.persist(closedMeeting);
        entityManager.persist(freeScheduledMeeting);
        entityManager.persist(freeOrderMeeting);
        entityManager.persist(reservation);
    }

    @Test
    @DisplayName("정상 요청 시 201과 응답 반환")
    void saveReservationTest1() throws Exception {
        //given
        PostReservationDto reservationDto = PostReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
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
                .andExpect(jsonPath("$.amount").value(meeting.getPrice()))
                .andExpect(jsonPath("$.orderId").isString())
                .andExpect(jsonPath("$.orderName").value(meeting.getTitle()))
                .andExpect(jsonPath("$.customerEmail").value(userMail))
                .andExpect(jsonPath("$.customerName").value(userNickname))
                .andExpect(jsonPath("$.successUrl").isString())
                .andExpect(jsonPath("$.failUrl").isString());
    }

    @Test
    @DisplayName("존재하지 않는 meetingId 요청 시 404 반환")
    void saveReservationTest2() throws Exception {
        //given
        PostReservationDto reservationDto = PostReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
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
        perform.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("meeting이 closed 상태라면 409 반환")
    void saveReservationTest3() throws Exception {
        //given
        PostReservationDto reservationDto = PostReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
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
        perform.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("free meeting이고 동시간대에 이미 예약이 존재한다면 409 반환")
    void saveReservationTest4() throws Exception {
        //given
        PostReservationDto reservationDto = PostReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
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
        perform.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("요청 금액과 실제 서버에서 계산한 금액이 일치하지 않을 경우 409 반환")
    void saveReservationTest5() throws Exception {
        //given
        PostReservationDto reservationDto = PostReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
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
        perform.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("meeting에 정의된 시간이 아닐 경우 409 발생")
    void saveReservationTest6() throws Exception {
        //given
        PostReservationDto reservationDto = PostReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
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
        perform.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("meeting에 정의된 최대 연속 예약 시간을 넘어설 경우 409 발생")
    void saveReservationTest7() throws Exception {
        //given
        PostReservationDto reservationDto = PostReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
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
        perform.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("무료 모임일 경우 orderName, customerEmail, customerName, createDate, amount 외에는 Null 반환")
    void saveReservationTest8() throws Exception {
        //given
        PostReservationDto reservationDto = PostReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(0L)
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
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
                .andExpect(jsonPath("$.amount").value(0))
                .andExpect(jsonPath("$.orderId").isEmpty())
                .andExpect(jsonPath("$.orderName").value(meeting.getTitle()))
                .andExpect(jsonPath("$.customerEmail").value(userMail))
                .andExpect(jsonPath("$.customerName").value(userNickname))
                .andExpect(jsonPath("$.successUrl").isEmpty())
                .andExpect(jsonPath("$.failUrl").isEmpty());
    }

    @Test
    @DisplayName("date policy가 free가 아닐 경우 start time과 end time이 meeting과 일치해야함")
    void saveReservationTest9() throws Exception {
        //given
        PostReservationDto reservationDto = PostReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
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
        perform.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("정원이 가득찼을 경우 409 반환")
    void saveReservationTest10() throws Exception {
        //given
        PostReservationDto reservationDto = PostReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
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
        perform.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("정원이 가득찬 상태가 아니여도 같은 예약을 2회 요청 시 409 Conflict 반환")
    void saveReservationTest11() throws Exception {
        //given
        PostReservationDto reservationDto = PostReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.of(2022,1,5))
                        .startTime(LocalTime.of(10,0,0))
                        .endTime(LocalTime.of(18,0,0))
                        .build())
                .build();

        String json = objectMapper.writeValueAsString(reservationDto);

        //when
        ResultActions perform1 = mockMvc.perform(post("/meetings/{meetingId}/reservations", meeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        ResultActions perform2 = mockMvc.perform(post("/meetings/{meetingId}/reservations", meeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        //then
        perform2.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("FreeMeet의 경우 요청이 완전히 동일하지 않더라도 시간대가 겹치면 Conflict 발생")
    void saveReservationTest12() throws Exception {
        //given
        PostReservationDto reservationDto1 = PostReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(0L)
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.of(2022,1,5))
                        .startTime(LocalTime.of(10,0,0))
                        .endTime(LocalTime.of(12,0,0))
                        .build())
                .build();

        PostReservationDto reservationDto2 = PostReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(0L)
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.of(2022,1,5))
                        .startTime(LocalTime.of(11,0,0))
                        .endTime(LocalTime.of(13,0,0))
                        .build())
                .build();

        String json1 = objectMapper.writeValueAsString(reservationDto1);
        String json2 = objectMapper.writeValueAsString(reservationDto2);

        //when
        ResultActions perform1 = mockMvc.perform(post("/meetings/{meetingId}/reservations", freeOrderMeeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json1)
                .header(JWT_HEADER, accessToken));

        ResultActions perform2 = mockMvc.perform(post("/meetings/{meetingId}/reservations", freeOrderMeeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json2)
                .header(JWT_HEADER, accessToken));

        //then
        perform1.andExpect(status().isCreated());
        perform2.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("요청시 바디의 값이 유효하지 않으면 400 반환")
    void confirmReservationTest1() throws Exception {
        //given
        PatchReservationDto request = new PatchReservationDto("tee");
        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(patch("/meetings/{meetingId}/reservations/{reservationId}", meeting.getId(), reservation.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, hostToken));

        //then
        perform.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("요청시 해당 meetingId가 존재하지 않는다면 404 반환")
    void confirmReservationTest2() throws Exception {
        //given
        PatchReservationDto request = new PatchReservationDto(Boolean.TRUE.toString());
        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(patch("/meetings/{meetingId}/reservations/{reservationId}", -1L, reservation.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, hostToken));

        //then
        perform.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("요청시 해당 reservationId가 존재하지 않는다면 404 반환")
    void confirmReservationTest3() throws Exception {
        //given
        PatchReservationDto request = new PatchReservationDto(Boolean.TRUE.toString());
        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(patch("/meetings/{meetingId}/reservations/{reservationId}", meeting.getId(), -1L)
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, hostToken));

        //then
        perform.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("요청시 토큰의 id 정보가 주최자 id와 일치하지 않으면 403 반환")
    void confirmReservationTest4() throws Exception {
        //given
        PatchReservationDto request = new PatchReservationDto(Boolean.TRUE.toString());
        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(patch("/meetings/{meetingId}/reservations/{reservationId}", meeting.getId(), reservation.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        //then
        perform.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("거절 시 예약 상태가 이미 확정이라면 409 반환")
    void confirmReservationTest5() throws Exception {
        //given
        ReflectionTestUtils.setField(reservation, "reservationState", ReservationState.ACCEPT);

        PatchReservationDto request = new PatchReservationDto(Boolean.FALSE.toString());
        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(patch("/meetings/{meetingId}/reservations/{reservationId}", meeting.getId(), reservation.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, hostToken));

        //then
        perform.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("승인 시 예약 상태가 이미 취소라면 409 반환")
    void confirmReservationTest6() throws Exception {
        //given
        ReflectionTestUtils.setField(reservation, "reservationState", ReservationState.CANCEL);

        PatchReservationDto request = new PatchReservationDto(Boolean.TRUE.toString());
        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(patch("/meetings/{meetingId}/reservations/{reservationId}", meeting.getId(), reservation.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, hostToken));

        //then
        perform.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("정상 승인 시 해당 reservation의 상태가 ACCEPT로 변경된다.")
    void confirmReservationTest7() throws Exception {
        //given
        ReflectionTestUtils.setField(reservation, "reservationState", ReservationState.PAYMENT_SUCCESS);

        PatchReservationDto request = new PatchReservationDto(Boolean.TRUE.toString());
        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(patch("/meetings/{meetingId}/reservations/{reservationId}", meeting.getId(), reservation.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, hostToken));

        //then
        perform.andExpect(status().isOk());
        Assertions.assertThat(reservation.getReservationState()).isEqualTo(ReservationState.ACCEPT);
    }

    @Test
    @DisplayName("정상 거절 시 해당 reservation의 상태가 CANCEL로 변경된다.")
    void confirmReservationTest8() throws Exception {
        //given
        ReflectionTestUtils.setField(reservation, "reservationState", ReservationState.PAYMENT_SUCCESS);

        PatchReservationDto request = new PatchReservationDto(Boolean.FALSE.toString());
        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(patch("/meetings/{meetingId}/reservations/{reservationId}", meeting.getId(), reservation.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, hostToken));

        //then
        perform.andExpect(status().isOk());
        Assertions.assertThat(reservation.getReservationState()).isEqualTo(ReservationState.DENY);
    }

    @Test
    @DisplayName("성공적으로 예약을 수락/거절 시 이벤트가 1개 발행된다.")
    void mailEventListenerTest() throws Exception {
        //given
        ReflectionTestUtils.setField(reservation, "reservationState", ReservationState.PAYMENT_SUCCESS);

        PatchReservationDto request = new PatchReservationDto(Boolean.FALSE.toString());
        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(patch("/meetings/{meetingId}/reservations/{reservationId}", meeting.getId(), reservation.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, hostToken));

        //then
        long result = applicationEvents.stream(ReservationConfirmedEvent.class).count();
        Assertions.assertThat(result).isEqualTo(1L);
    }

    @Test
    @DisplayName("자신이 주최한 모임에 셀프 참여 시 409 예외 반환")
    void ownMeeting() throws Exception {
        //given
        User host = User.builder()
                .email(new Email(EMAIL2))
                .nickname(new Nickname(NICKNAME2))
                .build();
        em.persist(host);

        ReflectionTestUtils.setField(meeting, "hostId", host.getId());
        hostToken = jwtTokenUtil.createAccessToken(host.getEmail().getAddress(), host.getId(), List.of(ROLE_USER), host.getNickname().getNickname());

        PostReservationDto reservationDto = PostReservationDto.builder()
                .reservationMemo(CONTENT1)
                .amount(10000L)
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.of(2022,1,5))
                        .startTime(LocalTime.of(11,0,0))
                        .endTime(LocalTime.of(12,0,0))
                        .build())
                .build();

        String json = objectMapper.writeValueAsString(reservationDto);

        //when
        ResultActions perform = mockMvc.perform(post("/meetings/{meetingId}/reservations", meeting.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
                .header(JWT_HEADER, accessToken));

        //then
        perform.andExpect(status().isConflict());
    }
}
