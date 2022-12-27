package com.example.momobe.user.domain;

import com.example.momobe.MomoBeApplication;
import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.user.application.UserCommonService;
import com.example.momobe.user.application.UserFindService;
import com.example.momobe.user.dto.UserResponseDto;
import com.example.momobe.user.ui.UserController;
import com.example.momobe.user.mapper.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentRequest;
import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentResponse;
import static com.example.momobe.common.enums.TestConstants.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({UserController.class,UserMapper.class})
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityTestConfig.class)
@AutoConfigureRestDocs
@WithMockUser
@ContextConfiguration(classes = MomoBeApplication.class)
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
    private  UserMapper mapper;
    @MockBean
    private JwtArgumentResolver resolver;

    @Test
    @DisplayName("회원 정보 조회 테스트")
    public void getUser() throws Exception {
        //given
//        UserResponseDto userDto = UserResponseDto.builder()
//                .email(new Email("email@mail.com"))
//                .nickname(new Nickname("dodanmom"))
//                .point(new Point(100L))
//                .build();
        UserResponseDto userDto = UserResponseDto.builder()
                .email("email@mail.com")
                .nickname("dodanmom")
                .point(100L)
                .build();

        given(userCommonService.getUser(any())).willReturn(new User());
        given(mapper.userDtoOfUser(any(User.class))).willReturn(userDto);

        //when
        ResultActions actions =
                mockMvc.perform(
                        get("/mypage/profile")
                                .header(JWT_HEADER, BEARER_ACCESS_TOKEN)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );
        //then
       actions.andExpect(status().isOk())
                .andDo(document("user/getUser",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        REQUEST_HEADER_JWT,
                        responseFields(
                                fieldWithPath("nickname").type(STRING).description("닉네임"),
                                fieldWithPath("email").type(STRING).description("이메일"),
                                fieldWithPath("point").type(NUMBER).description("포인트")
                        )
                ));
    }

}
