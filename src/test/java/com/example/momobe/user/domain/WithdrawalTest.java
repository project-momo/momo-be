package com.example.momobe.user.domain;

import com.example.momobe.user.domain.enums.UserStateType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.testcontainers.shaded.org.awaitility.Awaitility.given;

public class WithdrawalTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("유효한 회원인지 확인")
    public void isValidUser() throws Exception {
        //given
        User user = User.builder()
                .email(new Email("email@mail.com"))
                .nickname(new Nickname("dodanmom"))
                .userState(new UserState(UserStateType.ACTIVE, LocalDateTime.now()))
                .build();

        given();

        //when
        ResultActions actions =
                mockMvc.perform(
                        delete("/mypage/profile")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization","token")
                );

        //then
    }

    @Test
    @DisplayName("회원 데이터 잔여 보존 기한 확인")
    public void confirmExpireDate(){
        //given
        //when
        //then
    }

}
