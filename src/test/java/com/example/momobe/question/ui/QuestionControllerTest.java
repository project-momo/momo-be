package com.example.momobe.question.ui;

import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.exception.ui.ExceptionController;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.question.domain.QuestionRepository;
import com.example.momobe.question.dto.in.QuestionDto;
import com.example.momobe.question.dto.out.ResponseQuestionDto;
import com.example.momobe.question.infrastructure.QuestionQueryRepository;
import com.example.momobe.question.mapper.QuestionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.aspectj.apache.bcel.generic.ObjectType.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureRestDocs
@Import({SecurityTestConfig.class, QuestionMapper.class})
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest({QuestionController.class, ExceptionController.class})
class QuestionControllerTest {
    @MockBean
    JwtArgumentResolver jwtArgumentResolver;

    @MockBean
    QuestionRepository questionRepository;

    @MockBean
    QuestionMapper questionMapper;

    @MockBean
    QuestionQueryRepository questionQueryRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("질문 글 작성 시 유효성 검사에 실패할 경우 400 반환")
    void postQuestion_failed_400() throws Exception {
        // given
        QuestionDto request = QuestionDto.builder().content(" ").build();
        String json = objectMapper.writeValueAsString(request);
        // when
        ResultActions perform = mockMvc.perform(post("/meetings/{meeting-id}/questions", 1L)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // then
        perform.andExpect(status().isBadRequest())
                .andDo(document("question_post/400",
                        requestHeaders(
                                headerWithName(JWT_HEADER).description("유효한 액세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("content").description("공백이거나 비어있음")
                        ))
                );
    }

    @Test
    @DisplayName("질문 글 작성 시 유효성 검사에 통과할 경우 201 반환")
    void postQuestion_failed_201() throws Exception {
        // given
        QuestionDto request = QuestionDto.builder().content(CONTENT1).build();
        String json = objectMapper.writeValueAsString(request);

        ResponseQuestionDto.User user = ResponseQuestionDto.User.builder()
                .userId(ID1)
                .nickname(NICKNAME1)
                .email(EMAIL1)
                .imageUrl(null)
                .build();

        ResponseQuestionDto.Answer answer = ResponseQuestionDto.Answer.builder()
                .answerer(user)
                .answerId(ID1)
                .content(CONTENT1)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        ResponseQuestionDto question = ResponseQuestionDto.builder()
                .questionId(ID1)
                .questioner(user)
                .answers(List.of(answer))
                .content(CONTENT1)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        BDDMockito.given(questionQueryRepository.getQuestions(any())).willReturn(List.of(question, question));

        // when
        ResultActions perform = mockMvc.perform(post("/meetings/{meeting-id}/questions", 1L)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // then
        perform.andExpect(status().isCreated())
                .andDo(document("question_post/201",
                        requestHeaders(
                                headerWithName(JWT_HEADER).description("유효한 액세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("content").description("질문 글 내용")
                        ),
                        responseFields(
                                fieldWithPath("[].questionId").type(LONG).description("질문 아이디"),
                                fieldWithPath("[].content").type(STRING).description("질문 내용"),
                                fieldWithPath("[].questioner").type(OBJECT).description("질문자 정보"),
                                fieldWithPath("[].questioner.userId").type(LONG).description("질문자 아이디"),
                                fieldWithPath("[].questioner.email").type(STRING).description("질문자 이메일"),
                                fieldWithPath("[].questioner.nickname").type(STRING).description("질문자 닉네임"),
                                fieldWithPath("[].questioner.imageUrl").type(STRING).description("질문자 프로필 이미지"),
                                fieldWithPath("[].createdAt").type(STRING).description("질문 작성일"),
                                fieldWithPath("[].modifiedAt").type(STRING).description("질문 수정일"),
                                fieldWithPath("[].answers").type(OBJECT_ARRAY).description("답변 목록"),
                                fieldWithPath("[].answers[].answerId").type(STRING).description("답변 아이디"),
                                fieldWithPath("[].answers[].content").type(STRING).description("답변 내용"),
                                fieldWithPath("[].answers[].answerer").type(OBJECT).description("답변자 정보"),
                                fieldWithPath("[].answers[].answerer.userId").type(LONG).description("답변자 아이디"),
                                fieldWithPath("[].answers[].answerer.email").type(STRING).description("답변자 이메일"),
                                fieldWithPath("[].answers[].answerer.nickname").type(STRING).description("답변자 닉네임"),
                                fieldWithPath("[].answers[].answerer.imageUrl").type(STRING).description("답변자 프로필 이미지"),
                                fieldWithPath("[].answers[].createdAt").type(STRING).description("답변 작성일"),
                                fieldWithPath("[].answers[].modifiedAt").type(STRING).description("답변 수정일")
                        ))
                ).andDo(
                        print()
                );
    }

    @Test
    @DisplayName("질문/답변 글 조회시 스테이터스 코드 200과 결과 반횐")
    void getQuestions_success_200() throws Exception {
        // given
        QuestionDto request = QuestionDto.builder().content(CONTENT1).build();
        String json = objectMapper.writeValueAsString(request);

        ResponseQuestionDto.User user = ResponseQuestionDto.User.builder()
                .userId(ID1)
                .nickname(NICKNAME1)
                .email(EMAIL1)
                .imageUrl(null)
                .build();

        ResponseQuestionDto.Answer answer = ResponseQuestionDto.Answer.builder()
                .answerer(user)
                .answerId(ID1)
                .content(CONTENT1)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        ResponseQuestionDto question = ResponseQuestionDto.builder()
                .questionId(ID1)
                .questioner(user)
                .answers(List.of(answer))
                .content(CONTENT1)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        BDDMockito.given(questionQueryRepository.getQuestions(any())).willReturn(List.of(question, question));

        // when
        ResultActions perform = mockMvc.perform(get("/meetings/{meeting-id}", 1L)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // then
        perform.andExpect(status().isOk())
                .andDo(document("question_get/200",
                        requestHeaders(
                                headerWithName(JWT_HEADER).description("유효한 액세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("content").description("질문 글 내용")
                        ),
                        responseFields(
                                fieldWithPath("[].questionId").type(LONG).description("질문 아이디"),
                                fieldWithPath("[].content").type(STRING).description("질문 내용"),
                                fieldWithPath("[].questioner").type(OBJECT).description("질문자 정보"),
                                fieldWithPath("[].questioner.userId").type(LONG).description("질문자 아이디"),
                                fieldWithPath("[].questioner.email").type(STRING).description("질문자 이메일"),
                                fieldWithPath("[].questioner.nickname").type(STRING).description("질문자 닉네임"),
                                fieldWithPath("[].questioner.imageUrl").type(STRING).description("질문자 프로필 이미지"),
                                fieldWithPath("[].createdAt").type(STRING).description("질문 작성일"),
                                fieldWithPath("[].modifiedAt").type(STRING).description("질문 수정일"),
                                fieldWithPath("[].answers").type(OBJECT_ARRAY).description("답변 목록"),
                                fieldWithPath("[].answers[].answerId").type(STRING).description("답변 아이디"),
                                fieldWithPath("[].answers[].content").type(STRING).description("답변 내용"),
                                fieldWithPath("[].answers[].answerer").type(OBJECT).description("답변자 정보"),
                                fieldWithPath("[].answers[].answerer.userId").type(LONG).description("답변자 아이디"),
                                fieldWithPath("[].answers[].answerer.email").type(STRING).description("답변자 이메일"),
                                fieldWithPath("[].answers[].answerer.nickname").type(STRING).description("답변자 닉네임"),
                                fieldWithPath("[].answers[].answerer.imageUrl").type(STRING).description("답변자 프로필 이미지"),
                                fieldWithPath("[].answers[].createdAt").type(STRING).description("답변 작성일"),
                                fieldWithPath("[].answers[].modifiedAt").type(STRING).description("답변 수정일")
                        ))
                ).andDo(
                        print()
                );
    }
}