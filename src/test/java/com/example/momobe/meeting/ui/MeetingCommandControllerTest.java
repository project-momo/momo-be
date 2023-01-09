package com.example.momobe.meeting.ui;

import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.meeting.application.MeetingCloseService;
import com.example.momobe.meeting.application.MeetingCommonService;
import com.example.momobe.meeting.application.MeetingUpdateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentRequest;
import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentResponse;
import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.enums.MeetingConstants.MEETING_UPDATE_DTO;
import static com.example.momobe.meeting.enums.MeetingConstants.PATH_PARAM_MEETING_ID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeetingCommandController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityTestConfig.class)
@AutoConfigureRestDocs
class MeetingCommandControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtArgumentResolver jwtArgumentResolver;
    @MockBean
    private MeetingCloseService meetingCommonService;
    @MockBean
    private MeetingUpdateService meetingUpdateService;

    @Test
    void updateMeeting() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(MEETING_UPDATE_DTO);

        // when
        ResultActions actions = mockMvc.perform(
                patch("/meetings/{meeting-id}", ID1)
                        .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                        .contentType(APPLICATION_JSON)
                        .content(content)
        );

        // then
        actions.andExpect(status().isOk())
                .andDo(document("meeting/update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        REQUEST_HEADER_JWT,
                        PATH_PARAM_MEETING_ID,
                        requestFields(
                                fieldWithPath("category").type(STRING).description("카테고리"),
                                fieldWithPath("title").type(STRING).description("제목"),
                                fieldWithPath("content").type(STRING).description("내용"),
                                fieldWithPath("tags").type(ARRAY).description("태그"),
                                fieldWithPath("address").type(OBJECT).description("주소"),
                                fieldWithPath("address.addressIds").type(ARRAY).description("주소 식별자"),
                                fieldWithPath("address.addressInfo").type(STRING).description("주소 정보"),
                                fieldWithPath("personnel").type(NUMBER).description("최대 예약 가능 인원"),
                                fieldWithPath("price").type(NUMBER).description("가격")
                        )
                ));
    }

    @Test
    void deleteMeeting() throws Exception {
        // given
        // when
        ResultActions actions = mockMvc.perform(
                delete("/meetings/{meeting-id}", ID1)
                        .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
        );

        // then
        actions.andExpect(status().isNoContent())
                .andDo(document("meeting/delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        REQUEST_HEADER_JWT,
                        PATH_PARAM_MEETING_ID
                ));
    }
}
