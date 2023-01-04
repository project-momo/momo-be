package com.example.momobe.meeting.ui;

import com.example.momobe.answer.mapper.AnswerMapper;
import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.enums.TestConstants;
import com.example.momobe.common.exception.ui.ExceptionController;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.meeting.dao.MeetingDatesQueryMapper;
import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.dto.ResponseMeetingDatesDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.apache.bcel.generic.ObjectType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.aspectj.apache.bcel.generic.ObjectType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureRestDocs
@Import({SecurityTestConfig.class})
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest({MeetingDatesQueryController.class, ExceptionController.class})
class MeetingDatesQueryControllerTest {
    @MockBean
    JwtArgumentResolver jwtArgumentResolver;

    @MockBean
    MeetingDatesQueryMapper meetingDatesQueryMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("예약 가능일 조회 테스트")
    void getMeetingDates() throws Exception {
        //given
        Long meetingId = 1L;
        LocalDate localDate = LocalDate.of(2022,1,1);
        ResponseMeetingDatesDto dto1 = ResponseMeetingDatesDto.builder()
                .dateTime(LocalDateTime.of(2022, 1, 1, 10, 0))
                .date(LocalDate.of(2022, 1, 1))
                .time(LocalTime.of(10, 0))
                .availability("true")
                .datePolicy(DatePolicy.PERIOD.toString())
                .category(Category.DESIGN.toString())
                .currentStaff(2)
                .maxTime(2)
                .personnel(4)
                .price(10000)
                .build();

        ResponseMeetingDatesDto dto2 = ResponseMeetingDatesDto.builder()
                .dateTime(LocalDateTime.of(2022, 1, 1, 11, 0))
                .date(LocalDate.of(2022, 1, 1))
                .time(LocalTime.of(11, 0))
                .availability("true")
                .datePolicy(DatePolicy.PERIOD.toString())
                .category(Category.DESIGN.toString())
                .currentStaff(2)
                .maxTime(4)
                .personnel(4)
                .price(10000)
                .build();

        List<ResponseMeetingDatesDto> respone = List.of(dto1, dto2);

        BDDMockito.given(meetingDatesQueryMapper.getTimes(meetingId, localDate.getMonthValue())).willReturn(respone);

        //when
        ResultActions perform = mockMvc.perform(get("/meetings/{meetingId}/reservations/dates/{date}", meetingId, localDate)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN));

        //then
        perform.andExpect(status().isOk())
                .andDo(document("getMeetingDates/200",
                        requestHeaders(
                                headerWithName(JWT_HEADER).description(ACCESS_TOKEN)
                        ),
                        responseFields(
                                fieldWithPath("[].dateTime").type(STRING).description("연월일 시분초"),
                                fieldWithPath("[].date").type(STRING).description("연월일"),
                                fieldWithPath("[].time").type(STRING).description("시분초"),
                                fieldWithPath("[].availability").type(STRING).description("예약 가능 여부"),
                                fieldWithPath("[].datePolicy").type(STRING).description("예약 정책"),
                                fieldWithPath("[].category").type(STRING).description("카테고리"),
                                fieldWithPath("[].currentStaff").type(INTEGER).description("현재 예약 인원수"),
                                fieldWithPath("[].maxTime").type(INTEGER).description("최대 예약 가능 시간"),
                                fieldWithPath("[].personnel").type(INTEGER).description("예약 정원"),
                                fieldWithPath("[].price").type(INTEGER).description("예약 금액 (자유일 경우 시간당 금액임을 유의)")
                        )
                        ));
    }
}