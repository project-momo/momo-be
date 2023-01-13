package com.example.momobe.meeting.dao;

import com.example.momobe.address.domain.Address;
import com.example.momobe.answer.domain.Answer;
import com.example.momobe.answer.domain.Content;
import com.example.momobe.answer.domain.Writer;
import com.example.momobe.common.config.JpaQueryFactoryConfig;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.example.momobe.meeting.dto.out.MeetingDetailResponseDto;
import com.example.momobe.question.domain.Question;
import com.example.momobe.question.infrastructure.QuestionQueryRepository;
import com.example.momobe.tag.domain.Tag;
import com.example.momobe.user.domain.Avatar;
import com.example.momobe.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.enums.MeetingConstants.generateMeeting;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureDataJpa
@Import(JpaQueryFactoryConfig.class)
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MeetingDetailQueryRepositoryTest {
    @Autowired
    private EntityManager em;

    private MeetingDetailQueryRepository meetingDetailQueryRepository;

    @BeforeEach
    void init() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QuestionQueryRepository questionQueryRepository = new QuestionQueryRepository(queryFactory);
        meetingDetailQueryRepository = new MeetingDetailQueryRepository(queryFactory, questionQueryRepository);
    }

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
        MeetingDetailResponseDto responseDto = meetingDetailQueryRepository.findById(meeting.getId());

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getMeetingId()).isEqualTo(meeting.getId());
        assertThat(responseDto.getCategory()).isEqualTo(meeting.getCategory().getDescription());
        assertThat(responseDto.getHost().getUserId()).isEqualTo(host.getId());
        assertThat(responseDto.getHost().getNickname()).isEqualTo(host.getNickname().getNickname());
        assertThat(responseDto.getHost().getImageUrl()).isEqualTo(host.getAvatar().getRemotePath());
        assertThat(responseDto.getTitle()).isEqualTo(meeting.getTitle());
        assertThat(responseDto.getContent()).isEqualTo(meeting.getContent());
        assertThat(responseDto.getAddress().getAddresses())
                .isEqualTo(List.of(address1.getSi() + " " + address1.getGu(),
                        address2.getSi() + " " + address2.getGu()));
        assertThat(responseDto.getAddress().getAddressInfo()).isEqualTo(meeting.getAddress().getAddressInfo());
        assertThat(responseDto.getMeetingState()).isEqualTo(meeting.getMeetingState().getDescription());
        assertThat(responseDto.getIsOpen()).isEqualTo(meeting.getMeetingState() == MeetingState.OPEN);
        assertThat(responseDto.getDateTime().getDatePolicy()).isEqualTo(meeting.getDateTimeInfo().getDatePolicy());
        assertThat(responseDto.getDateTime().getStartDate()).isEqualTo(meeting.getDateTimeInfo().getStartDate());
        assertThat(responseDto.getDateTime().getEndDate()).isEqualTo(meeting.getDateTimeInfo().getEndDate());
        assertThat(responseDto.getDateTime().getStartTime()).isEqualTo(meeting.getDateTimeInfo().getStartTime());
        assertThat(responseDto.getDateTime().getEndTime()).isEqualTo(meeting.getDateTimeInfo().getEndTime());
        assertThat(responseDto.getDateTime().getMaxTime()).isEqualTo(meeting.getDateTimeInfo().getMaxTime());
        assertThat(responseDto.getPrice()).isEqualTo(meeting.getPrice());
        assertThat(responseDto.getTags()).hasSize(2);
        assertThat(responseDto.getTags().get(0)).isIn(List.of(tag1.getName(), tag2.getName()));
        assertThat(responseDto.getTags().get(1)).isIn(List.of(tag1.getName(), tag2.getName()));

        assertThat(responseDto.getQuestions()).isNotEmpty();
        assertThat(responseDto.getQuestions().get(0).getQuestionId()).isEqualTo(question.getId());
        assertThat(responseDto.getQuestions().get(0).getContent()).isEqualTo(question.getContent().getContent());
        assertThat(responseDto.getQuestions().get(0).getQuestioner().getUserId()).isEqualTo(questioner.getId());
        assertThat(responseDto.getQuestions().get(0).getQuestioner().getEmail()).isEqualTo(questioner.getEmail().getAddress());
        assertThat(responseDto.getQuestions().get(0).getQuestioner().getNickname()).isEqualTo(questioner.getNickname().getNickname());
        assertThat(responseDto.getQuestions().get(0).getQuestioner().getImageUrl()).isEqualTo(questioner.getAvatar().getRemotePath());
        assertThat(responseDto.getQuestions().get(0).getCreatedAt()).isNotNull();
        assertThat(responseDto.getQuestions().get(0).getModifiedAt()).isNotNull();

        assertThat(responseDto.getQuestions().get(0).getAnswers()).isNotEmpty();
        assertThat(responseDto.getQuestions().get(0).getAnswers().get(0).getAnswerId()).isEqualTo(answer.getId());
        assertThat(responseDto.getQuestions().get(0).getAnswers().get(0).getContent()).isEqualTo(answer.getContent().getContent());
        assertThat(responseDto.getQuestions().get(0).getAnswers().get(0).getAnswerer().getUserId()).isEqualTo(answerer.getId());
        assertThat(responseDto.getQuestions().get(0).getAnswers().get(0).getAnswerer().getEmail()).isEqualTo(answerer.getEmail().getAddress());
        assertThat(responseDto.getQuestions().get(0).getAnswers().get(0).getAnswerer().getNickname()).isEqualTo(answerer.getNickname().getNickname());
        assertThat(responseDto.getQuestions().get(0).getAnswers().get(0).getCreatedAt()).isNotNull();
        assertThat(responseDto.getQuestions().get(0).getAnswers().get(0).getModifiedAt()).isNotNull();
    }

}
