package com.example.momobe.meeting.ui;

import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.meeting.application.MeetingRegistrationService;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.dto.in.MeetingRequestDto;
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
import static com.example.momobe.meeting.enums.MeetingConstants.MEETING_REQUEST_DTO_WITH_ALL;
import static com.example.momobe.meeting.enums.MeetingConstants.generateMeeting;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeetingRegistrationController.class)
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
    private MeetingRegistrationService meetingRegistrationService;
    @MockBean
    private JwtArgumentResolver jwtArgumentResolver;

    @Test
    void registerMeeting() throws Exception {
        // given
        Meeting meeting = generateMeeting();
        setField(meeting, "id", ID1);
        String content = objectMapper.writeValueAsString(MEETING_REQUEST_DTO_WITH_ALL);
        given(meetingRegistrationService.saveMeeting(eq(null), any(MeetingRequestDto.class)))
                .willReturn(meeting);

        // when
        ResultActions actions = mockMvc.perform(
                post("/meetings")
                        .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                        .contentType(APPLICATION_JSON)
                        .content(content)
        );

        // then
        actions.andExpect(status().isCreated())
                .andDo(document("meeting/registration",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        REQUEST_HEADER_JWT,
                        requestFields(
                                fieldWithPath("category").type(STRING).description("????????????"),
                                fieldWithPath("title").type(STRING).description("??????"),
                                fieldWithPath("content").type(STRING).description("??????"),
                                fieldWithPath("tags").type(ARRAY).description("??????"),
                                fieldWithPath("address").type(OBJECT).description("??????"),
                                fieldWithPath("address.addressIds").type(ARRAY).description("?????? ?????????"),
                                fieldWithPath("address.addressInfo").type(STRING).description("?????? ??????"),
                                fieldWithPath("dateTime").type(OBJECT).description("??????/?????? ??????"),
                                fieldWithPath("dateTime.datePolicy").type(STRING).description("?????? ?????? (ONE_DAY/PERIOD/FREE)"),
                                fieldWithPath("dateTime.startDate").type(STRING).description("?????? ??????"),
                                fieldWithPath("dateTime.endDate").type(STRING).description("????????? ??????"),
                                fieldWithPath("dateTime.startTime").type(STRING).description("?????? ??????"),
                                fieldWithPath("dateTime.endTime").type(STRING).description("????????? ??????"),
                                fieldWithPath("dateTime.dayWeeks").type(ARRAY).description("?????? (???: 1 ~ ???: 7, datePolicy??? PEROID??? ??????)"),
                                fieldWithPath("dateTime.dates").type(ARRAY).description("?????? (datePolicy??? FREE??? ??????)"),
                                fieldWithPath("dateTime.maxTime").type(NUMBER).description("?????? ?????? ?????? ??????"),
                                fieldWithPath("personnel").type(NUMBER).description("?????? ?????? ?????? ??????"),
                                fieldWithPath("price").type(NUMBER).description("??????")
                        )
                ));
    }

}