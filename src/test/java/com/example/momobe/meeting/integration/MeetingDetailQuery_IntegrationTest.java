package com.example.momobe.meeting.integration;


import com.example.momobe.address.domain.Address;
import com.example.momobe.answer.domain.Answer;
import com.example.momobe.answer.domain.Content;
import com.example.momobe.answer.domain.Writer;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.example.momobe.question.domain.Question;
import com.example.momobe.tag.domain.Tag;
import com.example.momobe.user.domain.Avatar;
import com.example.momobe.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.enums.MeetingConstants.generateMeeting;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
class MeetingDetailQuery_IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EntityManager em;

    @Test
    void meetingDetailQuery() throws Exception {
        // given
        User host = new User(EMAIL1, NICKNAME, PASSWORD1, new Avatar(REMOTE_PATH));
        em.persist(host);
        User questioner = new User(EMAIL2, NICKNAME, PASSWORD2, new Avatar(REMOTE_PATH));
        em.persist(questioner);
        User answerer = new User(EMAIL1, NICKNAME, PASSWORD1, new Avatar(REMOTE_PATH));
        em.persist(answerer);
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
        Tag tag1 = new Tag("온라인");
        Tag tag2 = new Tag("오프라인");
        em.persist(tag1);
        em.persist(tag2);
        Meeting meeting = generateMeeting(
                host.getId(), List.of(address1.getId(), address2.getId()), List.of(tag1.getId(), tag2.getId()));
        em.persist(meeting);
        Question question = new Question(meeting.getId(), CONTENT1, questioner.getId());
        em.persist(question);
        Answer answer = new Answer(
                new Content(CONTENT2),
                new com.example.momobe.answer.domain.Meeting(meeting.getId()),
                new Writer(answerer.getId()),
                new com.example.momobe.answer.domain.Question(question.getId()));
        em.persist(answer);

        // when
        ResultActions actions = mockMvc.perform(
                get("/meetings/{meeting-id}", meeting.getId()));

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.meetingId").value(meeting.getId()))
                .andExpect(jsonPath("$.category").value(meeting.getCategory().getDescription()))
                .andExpect(jsonPath("$.host.userId").value(host.getId()))
                .andExpect(jsonPath("$.host.nickname").value(host.getNickname().getNickname()))
                .andExpect(jsonPath("$.host.imageUrl").value(host.getAvatar().getRemotePath()))
                .andExpect(jsonPath("$.title").value(meeting.getTitle()))
                .andExpect(jsonPath("$.content").value(meeting.getContent()))
                .andExpect(jsonPath("$.address.addresses[0]").value(address1.getSi() + " " + address1.getGu()))
                .andExpect(jsonPath("$.address.addresses[1]").value(address2.getSi() + " " + address2.getGu()))
                .andExpect(jsonPath("$.address.addressInfo").value(meeting.getAddress().getAddressInfo()))
                .andExpect(jsonPath("$.meetingState").value(meeting.getMeetingState().getDescription()))
                .andExpect(jsonPath("$.isOpen").value(meeting.getMeetingState() == MeetingState.OPEN))
                .andExpect(jsonPath("$.dateTime.datePolicy").value(meeting.getDateTimeInfo().getDatePolicy().toString()))
                .andExpect(jsonPath("$.dateTime.startDate").value(meeting.getDateTimeInfo().getStartDate().toString()))
                .andExpect(jsonPath("$.dateTime.endDate").value(meeting.getDateTimeInfo().getEndDate().toString()))
                .andExpect(jsonPath("$.dateTime.startTime").value(meeting.getDateTimeInfo().getStartTime().format(DateTimeFormatter.ISO_LOCAL_TIME)))
                .andExpect(jsonPath("$.dateTime.endTime").value(meeting.getDateTimeInfo().getEndTime().format(DateTimeFormatter.ISO_LOCAL_TIME)))
                .andExpect(jsonPath("$.dateTime.maxTime").value(meeting.getDateTimeInfo().getMaxTime()))
                .andExpect(jsonPath("$.price").value(meeting.getPrice()))
                .andExpect(jsonPath("$.tags[0]").value(tag1.getName()))
                .andExpect(jsonPath("$.tags[1]").value(tag2.getName()))

                .andExpect(jsonPath("$.questions[0].questionId").value(question.getId()))
                .andExpect(jsonPath("$.questions[0].content").value(question.getContent().getContent()))
                .andExpect(jsonPath("$.questions[0].questioner.userId").value(questioner.getId()))
                .andExpect(jsonPath("$.questions[0].questioner.email").value(questioner.getEmail().getAddress()))
                .andExpect(jsonPath("$.questions[0].questioner.nickname").value(questioner.getNickname().getNickname()))
                .andExpect(jsonPath("$.questions[0].questioner.imageUrl").value(questioner.getAvatar().getRemotePath()))
                .andExpect(jsonPath("$.questions[0].createdAt").isString())
                .andExpect(jsonPath("$.questions[0].modifiedAt").isString())

                .andExpect(jsonPath("$.questions[0].answers[0].answerId").value(answer.getId()))
                .andExpect(jsonPath("$.questions[0].answers[0].content").value(answer.getContent().getContent()))
                .andExpect(jsonPath("$.questions[0].answers[0].answerer.userId").value(answerer.getId()))
                .andExpect(jsonPath("$.questions[0].answers[0].answerer.email").value(answerer.getEmail().getAddress()))
                .andExpect(jsonPath("$.questions[0].answers[0].answerer.nickname").value(answerer.getNickname().getNickname()))
                .andExpect(jsonPath("$.questions[0].answers[0].answerer.imageUrl").value(answerer.getAvatar().getRemotePath()))
                .andExpect(jsonPath("$.questions[0].answers[0].createdAt").isString())
                .andExpect(jsonPath("$.questions[0].answers[0].modifiedAt").isString());
    }

}
