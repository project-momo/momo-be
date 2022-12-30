package com.example.momobe.meeting.ui;

import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.meeting.dao.MeetingQueryRepository;
import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.dto.MeetingResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentRequest;
import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentResponse;
import static com.example.momobe.common.enums.PageConstants.*;
import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.domain.enums.Category.SOCIAL;
import static com.example.momobe.meeting.domain.enums.MeetingState.OPEN;
import static com.example.momobe.meeting.enums.MeetingConstant.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeetingQueryController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityTestConfig.class)
@AutoConfigureRestDocs
class MeetingQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtArgumentResolver jwtArgumentResolver;
    @MockBean
    private MeetingQueryRepository meetingQueryRepository;

    @Test
    public void meetingQuery() throws Exception {
        // given
        MeetingResponseDto meetingResponseDto = new MeetingResponseDto(
                ID1, SOCIAL, ID1, NICKNAME, REMOTE_PATH, TITLE1, CONTENT1, ADDRESS1, OPEN,
                DatePolicy.FREE, START_DATE, END_DATE, START_TIME, END_TIME, 3, 1000L
        );
        meetingResponseDto.init(
                List.of("서울시 강남구", "서울시 강북구"),
                List.of(1, 3, 7), List.of(LocalDate.now(), LocalDate.now().plusDays(1)));
        PageRequest pageRequest = PageRequest.of(PAGE - 1, SIZE);

        given(meetingQueryRepository.findAll(eq(TITLE1), eq(SOCIAL), any(PageRequest.class)))
                .willReturn(new PageImpl<>(List.of(meetingResponseDto), pageRequest, 1L));

        // when
        ResultActions actions = mockMvc.perform(
                get("/meetings")
                        .param("keyword", TITLE1)
                        .param("category", String.valueOf(SOCIAL))
                        .param("page", String.valueOf(PAGE))
                        .param("size", String.valueOf(SIZE))
        );

        // then
        actions.andExpect(status().isOk())
                .andDo(document("meeting/query",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("keyword").description("검색어"),
                                parameterWithName("category").description("카테고리"),
                                PWN_PAGE, PWN_SIZE
                        ),
                        responseFields(
                                FWP_CONTENT, FWP_CONTENT_MEETING_ID, FWP_CONTENT_CATEGORY,
                                FWP_CONTENT_HOST, FWP_CONTENT_HOST_USER_ID, FWP_CONTENT_HOST_NICKNAME, FWP_CONTENT_HOST_IMAGE_URL,
                                FWP_CONTENT_TITLE, FWP_CONTENT_CONTENT,
                                FWP_CONTENT_ADDRESS, FWP_CONTENT_ADDRESS_ADDRESSES, FWP_CONTENT_ADDRESS_ADDRESS_INFO,
                                FWP_CONTENT_MEETING_STATE, FWP_CONTENT_IS_OPEN,
                                FWP_CONTENT_DATE_TIME, FWP_CONTENT_DATE_TIME_DATE_POLICY, FWP_CONTENT_DATE_TIME_START_DATE,
                                FWP_CONTENT_DATE_TIME_END_DATE, FWP_CONTENT_DATE_TIME_START_TIME, FWP_CONTENT_DATE_TIME_END_TIME,
                                FWP_CONTENT_DATE_TIME_MAX_TIME, FWP_CONTENT_DATE_TIME_DAY_WEEKS, FWP_CONTENT_DATE_TIME_DATES,
                                FWP_CONTENT_PRICE,

                                FWP_PAGE_INFO, FWP_PAGE, FWP_SIZE, FWP_TOTAL_ELEMENTS, FWP_TOTAL_PAGES
                        )

                ));
    }

}