package com.example.momobe.meeting.integration;

import com.example.momobe.address.domain.Address;
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

import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.common.enums.TestConstants.PASSWORD1;
import static com.example.momobe.meeting.enums.MeetingConstants.generateMeeting;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class MeetingQuery_IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EntityManager em;

    @Test
    public void meetingQuery() throws Exception {
        // given
        User user = new User(EMAIL1, NICKNAME, PASSWORD1, new Avatar(REMOTE_PATH));
        em.persist(user);
        Address address1 = Address.builder()
                .si("서울시")
                .gu("강남구")
                .build();
        Address address2 = Address.builder()
                .si("서울시")
                .gu("강북구")
                .build();
        em.persist(address1);
        em.persist(address2);
        em.persist(generateMeeting(user.getId(), List.of(address1.getId(), address2.getId())));

        // when
        ResultActions actions = mockMvc.perform(
                get("/meetings")
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

}
