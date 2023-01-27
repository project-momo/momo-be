package com.example.momobe.reservation.ui;

import com.example.momobe.common.config.ApiDocumentUtils;
import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.exception.ui.ExceptionController;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.meeting.domain.MeetingNotFoundException;
import com.example.momobe.payment.domain.UnableProceedPaymentException;
import com.example.momobe.reservation.application.ReservationCancelService;
import com.example.momobe.reservation.application.ReservationConfirmService;
import com.example.momobe.reservation.application.ReservationBookService;
import com.example.momobe.reservation.domain.ReservationException;
import com.example.momobe.reservation.dto.in.DeleteReservationDto;
import com.example.momobe.reservation.dto.in.PatchReservationDto;
import com.example.momobe.reservation.dto.in.PostReservationDto;
import com.example.momobe.reservation.dto.out.PaymentResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.common.exception.enums.ErrorCode.*;
import static org.aspectj.apache.bcel.generic.ObjectType.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureRestDocs
@Import(SecurityTestConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest({ReservationCommonController.class, ExceptionController.class})
class ReservationCommonControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReservationBookService reservationBookService;

    @MockBean
    ReservationConfirmService reservationConfirmService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    JwtArgumentResolver jwtArgumentResolver;

    @MockBean
    ReservationCancelService reservationCancelService;

    @Test
    @DisplayName("유효성 검사에 실패할 경우 400 반환")
    void postReservation_fail1() throws Exception {
        //given
        PostReservationDto request = PostReservationDto.builder()
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
                        .build())
                .reservationMemo(CONTENT1)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(post("/meetings/{meetingId}/reservations", 1)
                .contentType(APPLICATION_JSON)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .content(json));

        //then
        perform.andExpect(status().isBadRequest())
                .andDo(document("postReservation/400",
                                ApiDocumentUtils.getDocumentRequest(),
                                ApiDocumentUtils.getDocumentResponse(),
                                requestHeaders(
                                        headerWithName(JWT_HEADER).description(ACCESS_TOKEN)
                                ),
                                requestFields(
                                        fieldWithPath("dateInfo.reservationDate").description("비어있을 수 없습니다."),
                                        fieldWithPath("dateInfo.startTime").description("비어있을 수 없습니다."),
                                        fieldWithPath("dateInfo.endTime").description("비어있을 수 없습니다."),
                                        fieldWithPath("amount").description("비어있을 수 없습니다."),
                                        fieldWithPath("reservationMemo").description("예약자가 남기는 메모")
                                )
                        )
                );
    }

    @Test
    @DisplayName("해당 예약 시간에 예약이 불가능한 경우 409 반환")
    void postReservation_fail2() throws Exception {
        //given
        given(reservationBookService.reserve(anyLong(), any(PostReservationDto.class), any()))
                .willThrow(new ReservationException(FULL_OF_PEOPLE));

        PostReservationDto request = PostReservationDto.builder()
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.now())
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now().plus(1, ChronoUnit.HOURS))
                        .build())
                .amount(1000L)
                .reservationMemo(CONTENT1)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(post("/meetings/{meetingId}/reservations", 1)
                .contentType(APPLICATION_JSON)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .content(json));

        //then
        perform.andExpect(status().isConflict())
                .andDo(document("postReservation/409/full",
                                ApiDocumentUtils.getDocumentRequest(),
                                ApiDocumentUtils.getDocumentResponse(),
                                requestHeaders(
                                        headerWithName(JWT_HEADER).description(ACCESS_TOKEN)
                                ),
                                requestFields(
                                        fieldWithPath("dateInfo.reservationDate").description("예약일"),
                                        fieldWithPath("dateInfo.startTime").description("예약 시작 시간"),
                                        fieldWithPath("dateInfo.endTime").description("예약 종료 시간"),
                                        fieldWithPath("amount").description("비용"),
                                        fieldWithPath("reservationMemo").description("예약자가 남기는 메모")
                                )
                        )
                );
    }

    @Test
    @DisplayName("요청 시간과 예약 가능한 시간이 일치하지 않음 (시간대 자체가 올바르지 않음) 409 반환")
    void postReservation_fail3() throws Exception {
        //given
        given(reservationBookService.reserve(anyLong(), any(PostReservationDto.class), any()))
                .willThrow(new ReservationException(INVALID_RESERVATION_TIME));

        PostReservationDto request = PostReservationDto.builder()
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.now())
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now().plus(1, ChronoUnit.HOURS))
                        .build())
                .amount(1000L)
                .reservationMemo(CONTENT1)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(post("/meetings/{meetingId}/reservations", 1)
                .contentType(APPLICATION_JSON)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .content(json));

        //then
        perform.andExpect(status().isConflict())
                .andDo(document("postReservation/409/invalid",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(JWT_HEADER).description(ACCESS_TOKEN)
                        ),
                        requestFields(
                                fieldWithPath("dateInfo.reservationDate").description("예약할 수 없는 예약일/시간 (요청 자체가 올바르지 않음을 의미)"),
                                fieldWithPath("dateInfo.startTime").description("예약할 수 없는 예약일/시간 (요청 자체가 올바르지 않음을 의미)"),
                                fieldWithPath("dateInfo.endTime").description("예약할 수 없는 예약일/시간 (요청 자체가 올바르지 않음을 의미)"),
                                fieldWithPath("amount").description("비용"),
                                fieldWithPath("reservationMemo").description("예약자가 남기는 메모")
                                )
                        )
                );
    }

    @Test
    @DisplayName("예약 요청 시 신청한 금액과 실제 결제해야할 금액이 일치하지 않을 경우 409 반환")
    void postReservation_fail4() throws Exception {
        //given
        given(reservationBookService.reserve(anyLong(), any(PostReservationDto.class), any()))
                .willThrow(new ReservationException(AMOUNT_DOSE_NOT_MATCH));

        PostReservationDto request = PostReservationDto.builder()
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.now())
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now().plus(1, ChronoUnit.HOURS))
                        .build())
                .amount(1000L)
                .reservationMemo(CONTENT1)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(post("/meetings/{meetingId}/reservations", 1)
                .contentType(APPLICATION_JSON)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .content(json));

        //then
        perform.andExpect(status().isConflict())
                .andDo(document("postReservation/409/money",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(JWT_HEADER).description(ACCESS_TOKEN)
                        ),
                        requestFields(
                                fieldWithPath("dateInfo.reservationDate").description("예약일"),
                                fieldWithPath("dateInfo.startTime").description("예약 시작 시간"),
                                fieldWithPath("dateInfo.endTime").description("예약 종료 시간"),
                                fieldWithPath("amount").description("서버에서 계산한 금액과 일치하지 않음"),
                                fieldWithPath("reservationMemo").description("예약자가 남기는 메모")
                        )
                        )
                );
    }

    @Test
    @DisplayName("정상 요청의 경우")
    void postReservation_success() throws Exception {
        //given
        PostReservationDto request = PostReservationDto.builder()
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.now())
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now().plus(1, ChronoUnit.HOURS))
                        .build())
                .amount(1000L)
                .reservationMemo(CONTENT1)
                .build();

        PaymentResponseDto response = PaymentResponseDto.builder()
                .orderName(CONTENT1)
                .orderId(ID)
                .successUrl("/testpage")
                .failUrl("/testpage")
                .customerName(NICKNAME)
                .amount(1000L)
                .customerEmail(EMAIL1)
                .build();

        String json = objectMapper.writeValueAsString(request);
        given(reservationBookService.reserve(anyLong(), any(PostReservationDto.class), any())).willReturn(response);

        //when
        ResultActions perform = mockMvc.perform(post("/meetings/{meetingId}/reservations", 1)
                .contentType(APPLICATION_JSON)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .content(json));

        //then
        perform.andExpect(status().isCreated())
                .andDo(document("postReservation/201",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(JWT_HEADER).description(ACCESS_TOKEN)
                        ),
                        requestFields(
                                fieldWithPath("dateInfo.reservationDate").description("예약일"),
                                fieldWithPath("dateInfo.startTime").description("예약 시작 시간"),
                                fieldWithPath("dateInfo.endTime").description("예약 종료 시간"),
                                fieldWithPath("amount").description("서버에서 계산한 금액과 일치하지 않음"),
                                fieldWithPath("reservationMemo").description("예약자가 남기는 메모")
                        ),
                        responseFields(
                                fieldWithPath("amount").type(LONG).description("결제 금액"),
                                fieldWithPath("orderId").type(STRING).description("고유 결제 아이디"),
                                fieldWithPath("orderName").type(STRING).description("결제 항목 이름"),
                                fieldWithPath("customerEmail").type(STRING).description("고객 이메일 주소"),
                                fieldWithPath("customerName").type(STRING).description("고객 이름"),
                                fieldWithPath("successUrl").type(STRING).description("성공 시 이동 url"),
                                fieldWithPath("failUrl").type(STRING).description("실패 시 이동 url")
                        )
                        )
                );
    }

    @Test
    @DisplayName("요청한 meeting을 찾지 못할 경우 404 반환")
    void postReservation_fail5() throws Exception {
        //given
        given(reservationBookService.reserve(anyLong(), any(PostReservationDto.class), any()))
                .willThrow(new MeetingNotFoundException(DATA_NOT_FOUND));

        PostReservationDto request = PostReservationDto.builder()
                .dateInfo(PostReservationDto.ReservationDateDto.builder()
                        .reservationDate(LocalDate.now())
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now().plus(1, ChronoUnit.HOURS))
                        .build())
                .amount(1000L)
                .reservationMemo(CONTENT1)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(post("/meetings/{meetingId}/reservations", 1)
                .contentType(APPLICATION_JSON)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .content(json));

        //then
        perform.andExpect(status().isNotFound())
                .andDo(document("postReservation/404",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(JWT_HEADER).description(ACCESS_TOKEN)
                        ),
                        requestFields(
                                fieldWithPath("dateInfo.reservationDate").description("예약일"),
                                fieldWithPath("dateInfo.startTime").description("시작 시간"),
                                fieldWithPath("dateInfo.endTime").description("마지막 시간"),
                                fieldWithPath("amount").description("비용"),
                                fieldWithPath("reservationMemo").description("예약자가 남기는 메모")
                        )
                        )
                );
    }

    @Test
    @DisplayName("예약 승인/거절 400 반환 시나리오")
    void confirm_fail1() throws Exception {
        //given
        PatchReservationDto request = new PatchReservationDto("truw");
        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(patch("/meetings/{meetingId}/reservations/{reservationId}", 1, 1)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(json));

        //then
        perform.andExpect(status().isBadRequest())
                .andDo(document("patchReservation/400",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(JWT_HEADER).description(ACCESS_TOKEN)
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("모임 아이디"),
                                parameterWithName("reservationId").description("예약 아이디")
                        ),
                        requestFields(
                                fieldWithPath("isAccepted").description("요청은 반드시 대/소문자 상관 없이 true or false여야 합니다."),
                                fieldWithPath("message").description("반려 사유")
                        )
                        ));
    }

    @Test
    @DisplayName("예약 승인/거절 403 반환 시나리오")
    void confirm_fail2() throws Exception {
        //given
        PatchReservationDto request = new PatchReservationDto("true");
        String json = objectMapper.writeValueAsString(request);
        willThrow(new ReservationException(REQUEST_DENIED)).given(reservationConfirmService).confirm(anyLong(), anyLong(), any(), any(PatchReservationDto.class));

        //when
        ResultActions perform = mockMvc.perform(patch("/meetings/{meetingId}/reservations/{reservationId}",1L,1L)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .contentType(APPLICATION_JSON_VALUE)
                .content(json));

        //then
        perform.andExpect(status().isForbidden())
                .andDo(document("patchReservation/403",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(JWT_HEADER).description("해당 토큰의 id와 모임 주최자 id가 일치하지 않음")
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("모임 아이디"),
                                parameterWithName("reservationId").description("예약 아이디")
                        ),
                        requestFields(
                                fieldWithPath("isAccepted").description("요청은 반드시 대/소문자 상관 없이 true or false여야 합니다."),
                                fieldWithPath("message").description("반려 사유")
                        )
                ));
    }

    @Test
    @DisplayName("예약 승인/거절 404 반환 시나리오")
    void confirm_fail3() throws Exception {
        //given
        PatchReservationDto request = new PatchReservationDto("true");
        String json = objectMapper.writeValueAsString(request);
        willThrow(new MeetingNotFoundException(DATA_NOT_FOUND)).given(reservationConfirmService).confirm(anyLong(), anyLong(), any(), any(PatchReservationDto.class));

        //when
        ResultActions perform = mockMvc.perform(patch("/meetings/{meetingId}/reservations/{reservationId}",1L,1L)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .contentType(APPLICATION_JSON_VALUE)
                .content(json));

        //then
        perform.andExpect(status().isNotFound())
                .andDo(document("patchReservation/404",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(JWT_HEADER).description(ACCESS_TOKEN)
                        ),
                        pathParameters(
                               parameterWithName("meetingId").description("존재하지 않는 모임 아이디"),
                               parameterWithName("reservationId").description("존재하지 않는 예약 아이디")
                        ),
                        requestFields(
                                fieldWithPath("isAccepted").description("true(승인), false(거절)"),
                                fieldWithPath("message").description("반려 사유")
                        )
                ));
    }

    @Test
    @DisplayName("예약 승인/거절 409 반환 시나리오")
    void confirm_fail4() throws Exception {
        //given
        PatchReservationDto request = new PatchReservationDto("true");
        String json = objectMapper.writeValueAsString(request);
        willThrow(new ReservationException(CONFIRMED_RESERVATION)).given(reservationConfirmService).confirm(anyLong(), anyLong(), any(), any(PatchReservationDto.class));

        //when
        ResultActions perform = mockMvc.perform(patch("/meetings/{meetingId}/reservations/{reservationId}",1L,1L)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(json));

        //then
        perform.andExpect(status().isConflict())
                .andDo(document("patchReservation/409",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(JWT_HEADER).description(ACCESS_TOKEN)
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("모임 아이디"),
                                parameterWithName("reservationId").description("예약 아이디")
                        ),
                        requestFields(
                                fieldWithPath("isAccepted").description("true(승인), false(거절)"),
                                fieldWithPath("message").description("반려 사유")
                        )
                ));
    }

    @Test
    @DisplayName("예약 승인/거절 200 반환 시나리오")
    void confirm_success() throws Exception {
        //given
        PatchReservationDto request = new PatchReservationDto("true");
        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(patch("/meetings/{meetingId}/reservations/{reservationId}",1L,1L)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .contentType(APPLICATION_JSON_VALUE)
                .content(json));

        //then
        perform.andExpect(status().isOk())
                .andDo(document("patchReservation/200",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(JWT_HEADER).description(ACCESS_TOKEN)
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("모임 아이디"),
                                parameterWithName("reservationId").description("예약 아이디")
                        ),
                        requestFields(
                                fieldWithPath("isAccepted").description("true(승인), false(거절)"),
                                fieldWithPath("message").description("반려 사유")
                        )
                ));
    }

    @Test
    @DisplayName("예약 취소 400 시나리오")
    void cancel_fail_400() throws Exception {
        //given
        DeleteReservationDto request = DeleteReservationDto.builder()
                .paymentKey(" ")
                .cancelReason(" ")
                .build();
        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(delete("/meetings/{meetingId}/reservations/{reservationId}",1L,1L)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .contentType(APPLICATION_JSON_VALUE)
                .content(json));

        //then
        perform.andExpect(status().isBadRequest())
                .andDo(document("deleteReservation/400",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(JWT_HEADER).description(ACCESS_TOKEN)
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("모임 아이디"),
                                parameterWithName("reservationId").description("예약 아이디")
                        ),
                        requestFields(
                                fieldWithPath("paymentKey").description("paymentKey는 null, emtpy, white space일 수 없습니다."),
                                fieldWithPath("cancelReason").description("cancelReason은 null, emtpy, white space일 수 없습니다.")
                        )
                ));
    }

    @Test
    @DisplayName("예약 취소 403 시나리오")
    void cancel_fail_403() throws Exception {
        //given
        DeleteReservationDto request = DeleteReservationDto.builder()
                .paymentKey(CONTENT1)
                .cancelReason(CONTENT2)
                .build();
        String json = objectMapper.writeValueAsString(request);

        willThrow(new UnableProceedPaymentException(REQUEST_DENIED))
                .given(reservationCancelService).cancelReservation(any(), any(), any());

        //when
        ResultActions perform = mockMvc.perform(delete("/meetings/{meetingId}/reservations/{reservationId}",1L,1L)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .contentType(APPLICATION_JSON_VALUE)
                .content(json));

        //then
        perform.andExpect(status().isForbidden())
                .andDo(document("deleteReservation/403",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(JWT_HEADER).description("권한 없는 유저")
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("모임 아이디"),
                                parameterWithName("reservationId").description("예약 아이디")
                        ),
                        requestFields(
                                fieldWithPath("paymentKey").description("paymentKey"),
                                fieldWithPath("cancelReason").description("cancelReason")
                        )
                ));
    }

    @Test
    @DisplayName("예약 취소 404 시나리오")
    void cancel_fail_404() throws Exception {
        //given
        DeleteReservationDto request = DeleteReservationDto.builder()
                .paymentKey(CONTENT1)
                .cancelReason(CONTENT2)
                .build();
        String json = objectMapper.writeValueAsString(request);

        willThrow(new UnableProceedPaymentException(DATA_NOT_FOUND))
                .given(reservationCancelService).cancelReservation(any(), any(), any());

        //when
        ResultActions perform = mockMvc.perform(delete("/meetings/{meetingId}/reservations/{reservationId}",1L,1L)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .contentType(APPLICATION_JSON_VALUE)
                .content(json));

        //then
        perform.andExpect(status().isNotFound())
                .andDo(document("deleteReservation/404",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(JWT_HEADER).description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("모임 아이디"),
                                parameterWithName("reservationId").description("존재하지 않는 예약")
                        ),
                        requestFields(
                                fieldWithPath("paymentKey").description("paymentKey"),
                                fieldWithPath("cancelReason").description("cancelReason")
                        )
                ));
    }

    @Test
    @DisplayName("예약 취소 409 시나리오")
    void cancel_fail_409() throws Exception {
        //given
        DeleteReservationDto request = DeleteReservationDto.builder()
                .paymentKey(CONTENT1)
                .cancelReason(CONTENT2)
                .build();
        String json = objectMapper.writeValueAsString(request);

        willThrow(new UnableProceedPaymentException(CONFIRMED_RESERVATION))
                .given(reservationCancelService).cancelReservation(any(), any(), any());

        //when
        ResultActions perform = mockMvc.perform(delete("/meetings/{meetingId}/reservations/{reservationId}",1L,1L)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .contentType(APPLICATION_JSON_VALUE)
                .content(json));

        //then
        perform.andExpect(status().isConflict())
                .andDo(document("deleteReservation/409",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(JWT_HEADER).description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("모임 아이디"),
                                parameterWithName("reservationId").description("이미 승인 완료된 예약")
                        ),
                        requestFields(
                                fieldWithPath("paymentKey").description("paymentKey"),
                                fieldWithPath("cancelReason").description("cancelReason")
                        )
                ));
    }

    @Test
    @DisplayName("예약 취소 204 시나리오")
    void cancel_fail_204() throws Exception {
        //given
        DeleteReservationDto request = DeleteReservationDto.builder()
                .paymentKey(CONTENT1)
                .cancelReason(CONTENT2)
                .build();
        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(delete("/meetings/{meetingId}/reservations/{reservationId}",1L,1L)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .contentType(APPLICATION_JSON_VALUE)
                .content(json));

        //then
        perform.andExpect(status().isNoContent())
                .andDo(document("deleteReservation/204",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(JWT_HEADER).description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("모임 아이디"),
                                parameterWithName("reservationId").description("예약 아이디")
                        ),
                        requestFields(
                                fieldWithPath("paymentKey").description("paymentKey"),
                                fieldWithPath("cancelReason").description("cancelReason")
                        )
                ));
    }
}