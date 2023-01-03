package com.example.momobe.meeting.ui;

import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.meeting.dao.MeetingDetailQueryRepository;
import com.example.momobe.meeting.dao.MeetingQueryRepository;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.dto.MeetingDetailResponseDto;
import com.example.momobe.meeting.dto.MeetingResponseDto;
import com.example.momobe.question.dto.out.ResponseQuestionDto;
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
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;

import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentRequest;
import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentResponse;
import static com.example.momobe.common.enums.PageConstants.*;
import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.domain.enums.Category.SOCIAL;
import static com.example.momobe.meeting.domain.enums.MeetingState.OPEN;
import static com.example.momobe.meeting.enums.MeetingConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
    @MockBean
    private MeetingDetailQueryRepository meetingDetailQueryRepository;

    @Test
    public void meetingQuery() throws Exception {
        // given
        MeetingResponseDto meetingResponseDto = new MeetingResponseDto(
                ID1, SOCIAL, ID1, NICKNAME, EMAIL1, REMOTE_PATH, TITLE1, CONTENT1, SUB_ADDRESS1, OPEN,
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
                                FWP_CONTENT_HOST, FWP_CONTENT_HOST_USER_ID, FWP_CONTENT_HOST_NICKNAME,
                                FWP_CONTENT_HOST_IMAGE_URL, FWP_CONTENT_HOST_EMAIL,
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

    @Test
    public void meetingDetailQuery() throws Exception {
        // given
        ResponseQuestionDto responseQuestionDto = new ResponseQuestionDto(
                ID1, CONTENT1, ID2, EMAIL1, NICKNAME, REMOTE_PATH, LocalDateTime.now(), LocalDateTime.now(),
                List.of(new ResponseQuestionDto.Answer(
                        ID1, CONTENT1, ID3, EMAIL1, NICKNAME, REMOTE_PATH, LocalDateTime.now(), LocalDateTime.now()
                ))
        );

        MeetingDetailResponseDto meetingDetailResponseDto = new MeetingDetailResponseDto(
                ID1, SOCIAL, ID1, NICKNAME, REMOTE_PATH, EMAIL1, TITLE1, CONTENT1, SUB_ADDRESS1, OPEN,
                DatePolicy.FREE, START_DATE, END_DATE, START_TIME, END_TIME, 3, 1000L,
                new LinkedHashSet<>(List.of("서울시 강남구", "서울시 강북구")),
                List.of(LocalDateTime.now(), LocalDateTime.now().plusDays(1)));

        meetingDetailResponseDto.init(List.of(responseQuestionDto));

        given(meetingDetailQueryRepository.findById(ID1))
                .willReturn(meetingDetailResponseDto);

        // when
        ResultActions actions = mockMvc.perform(
                get("/meetings/{meeting-id}", ID1)
        );

        // then
        actions.andExpect(status().isOk())
                .andDo(document("meeting/query/detail",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("meeting-id").description("모임 식별자")
                        ),
                        responseFields(
                                fieldWithPath("meetingId").type(NUMBER).description("모임 식별자"),
                                fieldWithPath("category").type(STRING).description("카테고리"),
                                fieldWithPath("host").type(OBJECT).description("주최자"),
                                fieldWithPath("host.userId").type(NUMBER).description("주최자 식별자"),
                                fieldWithPath("host.nickname").type(STRING).description("주최자 닉네임"),
                                fieldWithPath("host.imageUrl").type(STRING).description("주최자 이미지"),
                                fieldWithPath("host.email").type(STRING).description("주최자 이메일"),
                                fieldWithPath("title").type(STRING).description("제목"),
                                fieldWithPath("content").type(STRING).description("내용"),
                                fieldWithPath("address").type(OBJECT).description("주소 정보"),
                                fieldWithPath("address.addresses").type(ARRAY).description("주소"),
                                fieldWithPath("address.addressInfo").type(STRING).description("추가 주소"),
                                fieldWithPath("meetingState").type(STRING).description("모임 상태"),
                                fieldWithPath("isOpen").type(BOOLEAN).description("모임 오픈 여부"),
                                fieldWithPath("dateTime").type(OBJECT).description("날짜 정보"),
                                fieldWithPath("dateTime.datePolicy").type(STRING).description("날짜 정책"),
                                fieldWithPath("dateTime.startDate").type(STRING).description("시작 날짜"),
                                fieldWithPath("dateTime.endDate").type(STRING).description("끝나는 날짜"),
                                fieldWithPath("dateTime.startTime").type(STRING).description("시작 시간"),
                                fieldWithPath("dateTime.endTime").type(STRING).description("끝나는 시간"),
                                fieldWithPath("dateTime.maxTime").type(NUMBER).description("최대 예약 가능 시간"),
                                fieldWithPath("dateTime.dayWeeks").type(ARRAY).description("요일 (월: 1 ~ 일: 7, datePolicy가 PEROID일 때만)"),
                                fieldWithPath("dateTime.dates").type(ARRAY).description("날짜 (datePolicy가 FREE일 때만)"),
                                fieldWithPath("price").type(NUMBER).description("가격"),

                                fieldWithPath("questions").type(ARRAY).description("질문/답변"),
                                fieldWithPath("questions[].questionId").type(NUMBER).description("질문 식별자"),
                                fieldWithPath("questions[].content").type(STRING).description("질문 내용"),
                                fieldWithPath("questions[].questioner").type(OBJECT).description("질문자"),
                                fieldWithPath("questions[].questioner.userId").type(NUMBER).description("질문자 식별자"),
                                fieldWithPath("questions[].questioner.email").type(STRING).description("질문자 이메일"),
                                fieldWithPath("questions[].questioner.nickname").type(STRING).description("질문자 닉네임"),
                                fieldWithPath("questions[].questioner.imageUrl").type(STRING).description("질문자 이미지"),
                                fieldWithPath("questions[].createdAt").type(STRING).description("질문 작성일시"),
                                fieldWithPath("questions[].modifiedAt").type(STRING).description("질문자 수정일시"),

                                fieldWithPath("questions[].answers").type(ARRAY).description("답변"),
                                fieldWithPath("questions[].answers[].answerId").type(NUMBER).description("답변 식별자"),
                                fieldWithPath("questions[].answers[].content").type(STRING).description("답변 내용"),
                                fieldWithPath("questions[].answers[].answerer").type(OBJECT).description("답변자"),
                                fieldWithPath("questions[].answers[].answerer.userId").type(NUMBER).description("답변자 식별자"),
                                fieldWithPath("questions[].answers[].answerer.email").type(STRING).description("답변자 이메일"),
                                fieldWithPath("questions[].answers[].answerer.nickname").type(STRING).description("답변자 닉네임"),
                                fieldWithPath("questions[].answers[].answerer.imageUrl").type(STRING).description("답변자 이미지"),
                                fieldWithPath("questions[].answers[].createdAt").type(STRING).description("답변 작성일시"),
                                fieldWithPath("questions[].answers[].modifiedAt").type(STRING).description("답변자 수정일시")
                        )
                ));
    }

}