package com.example.momobe.meeting.ui;

import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.meeting.application.MeetingCommonService;
import org.junit.jupiter.api.Test;
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
import static com.example.momobe.meeting.enums.MeetingConstants.PATH_PARAM_MEETING_ID;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeetingCommandController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityTestConfig.class)
@AutoConfigureRestDocs
class MeetingCommandControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtArgumentResolver jwtArgumentResolver;
    @MockBean
    private MeetingCommonService meetingCommonService;

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
