package com.example.momobe.question.ui;

import com.example.momobe.common.config.ApiDocumentUtils;
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
    @DisplayName("?????? ??? ?????? ??? ????????? ????????? ????????? ?????? 400 ??????")
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
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(JWT_HEADER).description("????????? ????????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("content").description("??????????????? ????????????")
                        ))
                );
    }

    @Test
    @DisplayName("?????? ??? ?????? ??? ????????? ????????? ????????? ?????? 201 ??????")
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
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(JWT_HEADER).description("????????? ????????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("content").description("?????? ??? ??????")
                        ),
                        responseFields(
                                fieldWithPath("[].questionId").type(LONG).description("?????? ?????????"),
                                fieldWithPath("[].content").type(STRING).description("?????? ??????"),
                                fieldWithPath("[].questioner").type(OBJECT).description("????????? ??????"),
                                fieldWithPath("[].questioner.userId").type(LONG).description("????????? ?????????"),
                                fieldWithPath("[].questioner.email").type(STRING).description("????????? ?????????"),
                                fieldWithPath("[].questioner.nickname").type(STRING).description("????????? ?????????"),
                                fieldWithPath("[].questioner.imageUrl").type(STRING).description("????????? ????????? ?????????"),
                                fieldWithPath("[].createdAt").type(STRING).description("?????? ?????????"),
                                fieldWithPath("[].modifiedAt").type(STRING).description("?????? ?????????"),
                                fieldWithPath("[].answers").type(OBJECT_ARRAY).description("?????? ??????"),
                                fieldWithPath("[].answers[].answerId").type(STRING).description("?????? ?????????"),
                                fieldWithPath("[].answers[].content").type(STRING).description("?????? ??????"),
                                fieldWithPath("[].answers[].answerer").type(OBJECT).description("????????? ??????"),
                                fieldWithPath("[].answers[].answerer.userId").type(LONG).description("????????? ?????????"),
                                fieldWithPath("[].answers[].answerer.email").type(STRING).description("????????? ?????????"),
                                fieldWithPath("[].answers[].answerer.nickname").type(STRING).description("????????? ?????????"),
                                fieldWithPath("[].answers[].answerer.imageUrl").type(STRING).description("????????? ????????? ?????????"),
                                fieldWithPath("[].answers[].createdAt").type(STRING).description("?????? ?????????"),
                                fieldWithPath("[].answers[].modifiedAt").type(STRING).description("?????? ?????????")
                        ))
                ).andDo(
                        print()
                );
    }

    @Test
    @DisplayName("??????/?????? ??? ????????? ??????????????? ?????? 200??? ?????? ??????")
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
        ResultActions perform = mockMvc.perform(get("/meetings/{meeting-id}/qna", 1L)
                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // then
        perform.andExpect(status().isOk())
                .andDo(document("question_get/200",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(JWT_HEADER).description("????????? ????????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("content").description("?????? ??? ??????")
                        ),
                        responseFields(
                                fieldWithPath("[].questionId").type(LONG).description("?????? ?????????"),
                                fieldWithPath("[].content").type(STRING).description("?????? ??????"),
                                fieldWithPath("[].questioner").type(OBJECT).description("????????? ??????"),
                                fieldWithPath("[].questioner.userId").type(LONG).description("????????? ?????????"),
                                fieldWithPath("[].questioner.email").type(STRING).description("????????? ?????????"),
                                fieldWithPath("[].questioner.nickname").type(STRING).description("????????? ?????????"),
                                fieldWithPath("[].questioner.imageUrl").type(STRING).description("????????? ????????? ?????????"),
                                fieldWithPath("[].createdAt").type(STRING).description("?????? ?????????"),
                                fieldWithPath("[].modifiedAt").type(STRING).description("?????? ?????????"),
                                fieldWithPath("[].answers").type(OBJECT_ARRAY).description("?????? ??????"),
                                fieldWithPath("[].answers[].answerId").type(STRING).description("?????? ?????????"),
                                fieldWithPath("[].answers[].content").type(STRING).description("?????? ??????"),
                                fieldWithPath("[].answers[].answerer").type(OBJECT).description("????????? ??????"),
                                fieldWithPath("[].answers[].answerer.userId").type(LONG).description("????????? ?????????"),
                                fieldWithPath("[].answers[].answerer.email").type(STRING).description("????????? ?????????"),
                                fieldWithPath("[].answers[].answerer.nickname").type(STRING).description("????????? ?????????"),
                                fieldWithPath("[].answers[].answerer.imageUrl").type(STRING).description("????????? ????????? ?????????"),
                                fieldWithPath("[].answers[].createdAt").type(STRING).description("?????? ?????????"),
                                fieldWithPath("[].answers[].modifiedAt").type(STRING).description("?????? ?????????")
                        ))
                ).andDo(
                        print()
                );
    }
}