package com.example.momobe.user.ui;

import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.exception.ui.ExceptionController;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.user.application.GuestTokenGenerateService;
import com.example.momobe.user.application.TokenGenerateService;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.dto.JwtTokenDto;
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

import static com.example.momobe.common.config.ApiDocumentUtils.*;
import static com.example.momobe.common.enums.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureRestDocs
@Import(SecurityTestConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest({GuestController.class, ExceptionController.class})
class GuestControllerTest {
    @MockBean
    JwtArgumentResolver jwtArgumentResolver;

    @MockBean
    GuestTokenGenerateService guestTokenGenerateService;

    @MockBean
    TokenGenerateService tokenGenerateService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("/guest")
    void getTestUserTest() throws Exception {
        //given
        User gueset = User.builder()
                .id(ID1)
                .build();

        JwtTokenDto response = new JwtTokenDto(BEARER_ACCESS_TOKEN, BEARER_REFRESH_TOKEN);
        given(guestTokenGenerateService.generate()).willReturn(gueset);
        given(tokenGenerateService.getJwtToken(gueset.getId())).willReturn(response);

        //when
        ResultActions result = mockMvc.perform(get("/auth/token/guest"));

        //then
        result.andExpect(status().isCreated())
                .andDo(document("auth/token/guest/200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseHeaders(
                                headerWithName(ACCESS_TOKEN).description("액세스 토큰입니다. 로컬 스토리지 저장을 권장합니다."),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰입니다. 쿠키 저장을 권장합니다.")
                        )
                        ));
    }
}