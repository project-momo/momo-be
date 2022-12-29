package com.example.momobe.meeting.integration;

import com.example.momobe.user.domain.Avatar;
import com.example.momobe.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.example.momobe.common.enums.TestConstants.REMOTE_PATH;
import static com.example.momobe.meeting.enums.MeetingConstant.generateMeeting;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class MeetingQuery_IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    EntityManager em;
    
    @Test
    public void meetingQuery() throws Exception {
        // given
        User user = (User.builder().avatar(new Avatar(REMOTE_PATH)).build());
        em.persist(user);
        em.persist(generateMeeting(user.getId()));

        // when
        ResultActions actions = mockMvc.perform(
                get("/meetings")
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andDo(print());
    }
    
}
