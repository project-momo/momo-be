package com.example.momobe.meeting.ui;

import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.MeetingRepository;
import com.example.momobe.meeting.mapper.DateTimeMapper;
import com.example.momobe.meeting.mapper.LocationMapper;
import com.example.momobe.meeting.mapper.MeetingMapper;
import com.example.momobe.meeting.mapper.PriceMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentRequest;
import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentResponse;
import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.common.util.ReflectionUtil.setField;
import static com.example.momobe.meeting.enums.MeetingConstant.MEETING_REQUEST_DTO_WITH_ALL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({MeetingRegistrationController.class,
        MeetingMapper.class, LocationMapper.class, DateTimeMapper.class, PriceMapper.class})
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityTestConfig.class)
@AutoConfigureRestDocs
@WithMockUser
class MeetingRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MeetingRepository meetingRepository;
    @MockBean
    private JwtArgumentResolver jwtArgumentResolver;

    @Test
    void registerMeeting() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(MEETING_REQUEST_DTO_WITH_ALL);
        given(meetingRepository.save(any(Meeting.class)))
                .willAnswer(args -> {
                    Meeting meeting = args.getArgument(0);
                    setField(meeting, "id", ID1);
                    return meeting;
                });

        // when
        ResultActions actions = mockMvc.perform(
                post("/meetings")
                        .content(content)
                        .contentType(APPLICATION_JSON)
                        .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
        );

        // then
        actions.andExpect(status().isCreated())
                .andDo(document("meeting/registration",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        REQUEST_HEADER_JWT,
                        requestFields(
                                fieldWithPath("category").type(STRING).description("카테고리"),
                                fieldWithPath("title").type(STRING).description("제목"),
                                fieldWithPath("content").type(STRING).description("내용"),
                                fieldWithPath("tags").type(ARRAY).description("태그"),
                                fieldWithPath("locations").type(ARRAY).description("장소"),
                                fieldWithPath("locations[].address1").type(STRING).description("주소1"),
                                fieldWithPath("locations[].address2").type(STRING).description("주소2"),
                                fieldWithPath("dateTime").type(OBJECT).description("날짜/시간 정보"),
                                fieldWithPath("dateTime.datePolicy").type(STRING).description("날짜 정책 (ONE_DAY/PERIOD/FREE)"),
                                fieldWithPath("dateTime.startDate").type(STRING).description("시작 날짜"),
                                fieldWithPath("dateTime.endDate").type(STRING).description("끝나는 날짜"),
                                fieldWithPath("dateTime.startTime").type(STRING).description("시작 시간"),
                                fieldWithPath("dateTime.endTime").type(STRING).description("끝나는 시간"),
                                fieldWithPath("dateTime.dayWeeks").type(ARRAY).description("요일 (월: 1 ~ 일: 7, datePolicy가 PEROID일 때만)"),
                                fieldWithPath("dateTime.dates").type(ARRAY).description("날짜 (datePolicy가 FREE일 때만)"),
                                fieldWithPath("priceInfo").type(OBJECT).description("가격 정보"),
                                fieldWithPath("priceInfo.pricePolicy").type(STRING).description("가격 정책"),
                                fieldWithPath("priceInfo.price").type(NUMBER).description("가격"),
                                fieldWithPath("notice").type(STRING).description("전달 사항")
                        )
                ));
    }

}