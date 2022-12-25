package com.example.momobe.meeting.ui;

import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.meeting.dao.MeetingQueryRepository;
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

import java.util.List;

import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentRequest;
import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentResponse;
import static com.example.momobe.common.enums.PageConstants.*;
import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.domain.enums.Category.MENTORING;
import static com.example.momobe.meeting.domain.enums.MeetingStatus.OPEN;
import static com.example.momobe.meeting.domain.enums.PricePolicy.HOUR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
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
                ID1, MENTORING, ID1, NICKNAME, TISTORY_URL, TITLE1, CONTENT1, ADDRESS1, OPEN, HOUR, 1000L
        );
        PageRequest pageRequest = PageRequest.of(PAGE - 1, SIZE);

        given(meetingQueryRepository.findAll(eq(TITLE1), eq(MENTORING), any(PageRequest.class)))
                .willReturn(new PageImpl<>(List.of(meetingResponseDto), pageRequest, 1L));

        // when
        ResultActions actions = mockMvc.perform(
                get("/meetings")
                        .param("keyword", TITLE1)
                        .param("category", String.valueOf(MENTORING))
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
                                FWP_CONTENT,
                                fieldWithPath("content[].meetingId").type(NUMBER).description("모임 식별자"),
                                fieldWithPath("content[].category").type(STRING).description("카테고리"),
                                fieldWithPath("content[].host").type(OBJECT).description("주최자 정보"),
                                fieldWithPath("content[].host.hostId").type(NUMBER).description("주최자 식별자"),
                                fieldWithPath("content[].host.nickname").type(STRING).description("주최자 닉네임"),
                                fieldWithPath("content[].host.imageUrl").type(STRING).description("주최자 이미지"),
                                fieldWithPath("content[].title").type(STRING).description("제목"),
                                fieldWithPath("content[].content").type(STRING).description("내용"),
                                fieldWithPath("content[].address").type(STRING).description("주소"),
                                fieldWithPath("content[].meetingStatus").type(STRING).description("모임 상태 (OPEN/CLOSE)"),
                                fieldWithPath("content[].pricePolicy").type(STRING).description("가격 정책 (HOUR/DAY)"),
                                fieldWithPath("content[].price").type(NUMBER).description("가격"),
                                FWP_PAGE_INFO, FWP_PAGE, FWP_SIZE, FWP_TOTAL_ELEMENTS, FWP_TOTAL_PAGES
                        )

                ));
    }

}