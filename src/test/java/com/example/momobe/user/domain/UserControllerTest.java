package com.example.momobe.user.domain;

import com.example.momobe.MomoBeApplication;
import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.user.application.UserCommonService;
import com.example.momobe.user.application.UserFindService;
import com.example.momobe.user.controller.UserController;
import com.example.momobe.user.domain.enums.UserStateType;
import com.example.momobe.user.mapper.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentRequest;
import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentResponse;
import static com.example.momobe.common.enums.TestConstants.ROLE_ADMIN;
import static com.example.momobe.common.enums.TestConstants.ROLE_USER;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({UserController.class,UserMapper.class})
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityTestConfig.class)
@AutoConfigureRestDocs
@WithMockUser
@ContextConfiguration(classes = MomoBeApplication.class)
//@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserCommonService userCommonService;

    @MockBean
    private UserFindService userFindService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtArgumentResolver resolver;

    @Test
    @DisplayName("회원 정보 조회 테스트")
    public void getUser() throws Exception {
        //given
        User user = User.builder()
                .email(new Email("email@mail.com"))
                .nickname(new Nickname("dodanmom"))
                .point(new Point(1000L))
                .build();
        UserInfo userInfo = UserInfo.builder()
                .id(1L)
                .email("email@mail.com")
                .roles(List.of(ROLE_USER,ROLE_ADMIN))
                .build();

        given(userCommonService.getUser(anyString())).willReturn(user);
        String content = objectMapper.writeValueAsString(userInfo);

        //when
        ResultActions actions =
                mockMvc.perform(
                        get("/mypage/profile")
                                .header("Authorization","token")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                );
        //then
       actions.andExpect(status().isOk())
//                .andExpect(jsonPath("$.nickname.nickname").value(user.getNickname().getNickname()))
//                .andExpect(jsonPath("$.email.address").value(user.getEmail().getAddress()))
//                .andExpect(jsonPath("$.point.point").value(user.getPoint().getPoint()))
                .andDo(document("user/getUser",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT"))
//                        responseFields(
//                                fieldWithPath("nickname.nickname").type(STRING).description("닉네임"),
//                                fieldWithPath("email.address").type(STRING).description("이메일"),
//                                fieldWithPath("point.point").type(NUMBER).description("포인트")
//                        )
                ));
    }

}
