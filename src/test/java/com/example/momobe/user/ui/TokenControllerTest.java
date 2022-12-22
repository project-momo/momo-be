package com.example.momobe.user.ui;

import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.enums.TestConstants;
import com.example.momobe.common.exception.ui.ExceptionController;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.user.application.LogoutService;
import com.example.momobe.user.domain.TokenNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
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
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
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
}