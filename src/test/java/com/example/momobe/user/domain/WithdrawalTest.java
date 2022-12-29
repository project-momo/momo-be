package com.example.momobe.user.domain;

import com.example.momobe.MomoBeApplication;
import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.user.application.UserCommonService;
import com.example.momobe.user.ui.UserWithdrawalController;
import com.example.momobe.user.domain.enums.UserStateType;
import com.example.momobe.user.mapper.UserMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentRequest;
import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentResponse;
import static com.example.momobe.common.enums.TestConstants.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UserWithdrawalController.class, UserMapper.class})
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityTestConfig.class)
@AutoConfigureRestDocs
@WithMockUser
@ContextConfiguration(classes = MomoBeApplication.class)
public class WithdrawalTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserCommonService userCommonService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtArgumentResolver resolver;

    @Test
    @DisplayName("유효한 회원인지 확인")
    public void isValidUser() throws Exception {
        //given
        User user = User.builder()
                .email(new Email("email@mail.com"))
                .nickname(new Nickname("dodanmom"))
                .userState(new UserState(UserStateType.ACTIVE, LocalDateTime.now()))
                .build();

        //when/then
        Assertions.assertThat(user.getUserState().isUserActive()).isTrue();
    }

    @Test
    @DisplayName("회원 데이터 잔여 보존 기한 확인")
    public void confirmExpireDate(){
        //given
        //when
        //then
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    public void withdrawalUser() throws Exception {
        //given
        User user = User.builder()
                .email(new Email("email@mail.com"))
                .nickname(new Nickname("dodanmom"))
                .userState(new UserState(UserStateType.ACTIVE, LocalDateTime.now()))
                .build();

        given(userCommonService.withdrawalUser(anyString())).willReturn(true);

        //when
        ResultActions actions =
                mockMvc.perform(
                        delete("/mypage/profile")
                                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                );
        //then
        actions.andExpect(status().isNoContent())
                .andDo(document("user/withdrawalUser",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        REQUEST_HEADER_JWT
                        ));
    }
}
