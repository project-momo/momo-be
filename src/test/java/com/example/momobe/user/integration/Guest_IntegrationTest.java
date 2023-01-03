package com.example.momobe.user.integration;

import com.example.momobe.common.enums.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest(value = {
        "jwt.secretKey=only_test_secret_Key_value_gn..rlfdlrkqnwhrgkekspdy",
        "jwt.refreshKey=only_test_refresh_key_value_gn..rlfdlrkqnwhrgkekspdy"
})
@AutoConfigureMockMvc
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
public class Guest_IntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void getGuestTokenTest1() throws Exception {
        //given
        //when
        ResultActions perform = mockMvc.perform(get("/auth/token/guest"));

        //then
        perform.andExpect(status().isCreated())
                .andExpect(header().exists(ACCESS_TOKEN))
                .andExpect(header().exists(REFRESH_TOKEN));
    }
}
