package com.example.momobe.user.ui;

import com.example.momobe.common.config.ApiDocumentUtils;
import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.exception.ui.ExceptionController;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.security.exception.SecurityException;
import com.example.momobe.user.application.LogoutService;
import com.example.momobe.user.application.TokenReissueService;
import com.example.momobe.user.domain.TokenNotFoundException;
import com.example.momobe.user.dto.JwtTokenDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
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

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.common.exception.enums.ErrorCode.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureRestDocs
@Import(SecurityTestConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest({TokenController.class, ExceptionController.class})
class TokenControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtArgumentResolver jwtArgumentResolver;

    @MockBean
    LogoutService logoutService;

    @MockBean
    TokenReissueService tokenReissueService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("???????????? ????????? ??????????????? ??????????????? ?????? 204 NoContent ??????")
    void deleteTokenTest_success() throws Exception {
        //given
        //when
        ResultActions perform = mockMvc.perform(delete("/auth/token")
                .header(REFRESH_TOKEN, BEARER_REFRESH_TOKEN));

        //then
        perform.andExpect(status().isNoContent())
                .andDo(document("token/delete/204",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(REFRESH_TOKEN).description("????????? ???????????? ??????")
                        )));
    }

    @Test
    @DisplayName("?????? ????????? ??? ?????? ????????? ???????????? ???????????? 404 ??????")
    void reissueTokenTest_failed_404() throws Exception {
        //given
        given(tokenReissueService.reIssueToken(anyString())).willThrow(new TokenNotFoundException(DATA_NOT_FOUND));
        //when
        ResultActions perform = mockMvc.perform(put("/auth/token")
                .header(REFRESH_TOKEN, BEARER_REFRESH_TOKEN));
        //then
        perform.andExpect(status().isNotFound())
                .andDo(document("token/put/404",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(REFRESH_TOKEN).description("???????????? ?????? ???????????? ??????")
                        )));
    }

    @Test
    @DisplayName("?????? ????????? ??? ?????? ????????? ???????????? ????????? 401 ??????")
    void reissueTokenTest_failed_401() throws Exception {
        //given
        given(tokenReissueService.reIssueToken(anyString())).willThrow(new SecurityException(MALFORMED_EXCEPTION));
        //when
        ResultActions perform = mockMvc.perform(put("/auth/token")
                .header(REFRESH_TOKEN, BEARER_REFRESH_TOKEN));
        //then
        perform.andExpect(status().isUnauthorized())
                .andDo(document("token/put/401",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(REFRESH_TOKEN).description("?????????????????? ???????????? ?????? ??????")
                        )));
    }

    @Test
    @DisplayName("?????? ????????? ?????? ??? 200 ??????????????? ????????? refreshToken, accessToken ??????")
    void reissueTokenTest_success_200() throws Exception {
        //given
        JwtTokenDto tokenDto = new JwtTokenDto(BEARER_ACCESS_TOKEN, BEARER_REFRESH_TOKEN);

        given(tokenReissueService.reIssueToken(anyString())).willReturn(tokenDto);
        //when
        ResultActions perform = mockMvc.perform(put("/auth/token")
                .header(REFRESH_TOKEN, BEARER_REFRESH_TOKEN));
        //then
        perform.andExpect(status().isOk())
                .andDo(document("token/put/200",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(REFRESH_TOKEN).description("????????? ???????????? ??????")
                        ),
                        responseHeaders(
                                headerWithName("accessToken").description("????????? ??????"),
                                headerWithName("refreshToken").description("???????????? ??????, ????????? ???????????? ?????? ???????????????!")
                        )));
    }
}