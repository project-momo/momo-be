package com.example.momobe.user.ui;

import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.enums.TestConstants;
import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.common.exception.ui.ExceptionController;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.security.exception.InvalidJwtTokenException;
import com.example.momobe.user.application.LogoutService;
import com.example.momobe.user.application.ReissueTokenService;
import com.example.momobe.user.domain.TokenNotFoundException;
import com.example.momobe.user.dto.JwtTokenDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
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
    ReissueTokenService reissueTokenService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("리프레시 토큰이 정상적으로 삭제되었을 경우 204 NoContent 반환")
    void deleteTokenTest_success() throws Exception {
        //given
        //when
        ResultActions perform = mockMvc.perform(delete("/auth/token")
                .header(REFRESH_TOKEN, BEARER_REFRESH_TOKEN));

        //then
        perform.andExpect(status().isNoContent())
                .andDo(document("token/delete/204",
                        requestHeaders(
                                headerWithName(REFRESH_TOKEN).description("유효한 리프레시 토큰")
                        )));
    }

    @Test
    @DisplayName("토큰 재발행 시 해당 토큰이 존재하지 않는다면 404 반환")
    void reissueTokenTest_failed_404() throws Exception {
        //given
        given(reissueTokenService.reIssueToken(anyString())).willThrow(new TokenNotFoundException(DATA_NOT_FOUND));
        //when
        ResultActions perform = mockMvc.perform(put("/auth/token")
                .header(REFRESH_TOKEN, BEARER_REFRESH_TOKEN));
        //then
        perform.andExpect(status().isNotFound())
                .andDo(document("token/put/404",
                        requestHeaders(
                                headerWithName(REFRESH_TOKEN).description("존재하지 않는 리프레시 토큰")
                        )));
    }

    @Test
    @DisplayName("토큰 재발행 시 해당 토큰이 유효하지 않다면 401 반환")
    void reissueTokenTest_failed_401() throws Exception {
        //given
        given(reissueTokenService.reIssueToken(anyString())).willThrow(new InvalidJwtTokenException(MALFORMED_EXCEPTION));
        //when
        ResultActions perform = mockMvc.perform(put("/auth/token")
                .header(REFRESH_TOKEN, BEARER_REFRESH_TOKEN));
        //then
        perform.andExpect(status().isUnauthorized())
                .andDo(document("token/put/401",
                        requestHeaders(
                                headerWithName(REFRESH_TOKEN).description("만료되었거나 유효하지 않은 토큰")
                        )));
    }

    @Test
    @DisplayName("토큰 재발행 성공 시 200 스테이터스 코드와 refreshToken, accessToken 반환")
    void reissueTokenTest_success_200() throws Exception {
        //given
        JwtTokenDto tokenDto = new JwtTokenDto(BEARER_ACCESS_TOKEN, BEARER_REFRESH_TOKEN);

        given(reissueTokenService.reIssueToken(anyString())).willReturn(tokenDto);
        //when
        ResultActions perform = mockMvc.perform(put("/auth/token")
                .header(REFRESH_TOKEN, BEARER_REFRESH_TOKEN));
        //then
        perform.andExpect(status().isOk())
                .andDo(document("token/put/200",
                        requestHeaders(
                                headerWithName(REFRESH_TOKEN).description("유효한 리프레시 토큰")
                        ),
                        responseHeaders(
                                headerWithName("accessToken").description("액세스 토큰"),
                                headerWithName("refreshToken").description("리프레시 토큰, 쿠키에 저장하는 것을 권장합니다!")
                        )));
    }
}