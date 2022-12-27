package com.example.momobe.question.infrastructure;

import com.example.momobe.answer.domain.Answer;
import com.example.momobe.answer.domain.AnswerRepository;
import com.example.momobe.common.config.JpaQueryFactoryConfig;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.MeetingRepository;
import com.example.momobe.meeting.domain.PriceInfo;
import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.MeetingStatus;
import com.example.momobe.meeting.domain.enums.PricePolicy;
import com.example.momobe.question.domain.Content;
import com.example.momobe.question.domain.Question;
import com.example.momobe.question.domain.QuestionRepository;
import com.example.momobe.question.domain.Writer;
import com.example.momobe.question.dto.out.ResponseQuestionDto;
import com.example.momobe.user.domain.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureDataJpa
@Import(JpaQueryFactoryConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionQueryRepositoryTest {
    @Autowired
    EntityManager em;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;

    QuestionQueryRepository questionQueryRepository;

    @BeforeEach
    void init() {
        questionQueryRepository = new QuestionQueryRepository(new JPAQueryFactory(em));
    }

    @Test
    @DisplayName("저장된 질문과 답변 조회 테스트")
    void getQuestionsTest1() {
        //given
        Meeting meeting = Meeting.builder()
                .title(TITLE1)
                .content(CONTENT1)
                .hostId(ID2)
                .category(Category.MEETING)
                .meetingStatus(MeetingStatus.OPEN)
                .priceInfo(new PriceInfo(PricePolicy.DAY, 20000L))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.now())
                .endTime(LocalTime.now().plusHours(1))
                .build();
        meetingRepository.save(meeting);

        User questioner1 = User.builder()
                .email(new Email(EMAIL1))
                .nickname(new Nickname(NICKNAME1))
                .avatar(null)
                .build();
        User questioner2 = User.builder()
                .email(new Email(EMAIL2))
                .nickname(new Nickname(NICKNAME2))
                .avatar(null)
                .build();
        User answerer = User.builder()
                .email(new Email(EMAIL2))
                .nickname(new Nickname(NICKNAME1))
                .avatar(new Avatar(TISTORY_URL))
                .build();
        userRepository.save(questioner1);
        userRepository.save(questioner2);
        userRepository.save(answerer);

        Question question1 = Question.builder()
                .meeting(new com.example.momobe.question.domain.Meeting(meeting.getId()))
                .content(new Content(CONTENT1))
                .writer(new Writer(questioner1.getId()))
                .build();
        questionRepository.save(question1);

        Question question2 = Question.builder()
                .meeting(new com.example.momobe.question.domain.Meeting(meeting.getId()))
                .content(new Content(CONTENT1))
                .writer(new Writer(questioner2.getId()))
                .build();
        questionRepository.save(question2);

        Answer answer1 = Answer.builder()
                .question(new com.example.momobe.answer.domain.Question(question1.getId()))
                .content(new com.example.momobe.answer.domain.Content(CONTENT2))
                .writer(new com.example.momobe.answer.domain.Writer(answerer.getId()))
                .build();

        Answer answer2 = Answer.builder()
                .question(new com.example.momobe.answer.domain.Question(question1.getId()))
                .content(new com.example.momobe.answer.domain.Content(CONTENT2))
                .writer(new com.example.momobe.answer.domain.Writer(answerer.getId()))
                .build();
        answerRepository.save(answer1);
        answerRepository.save(answer2);

        //when
        List<ResponseQuestionDto> result = questionQueryRepository.getQuestions(meeting.getId());
        System.out.print(result);

        //then
        assertThat(result.get(0).getQuestioner().getEmail()).isEqualTo(questioner2.getEmail().getAddress());
        assertThat(result.get(1).getQuestioner().getEmail()).isEqualTo(questioner1.getEmail().getAddress());
        assertThat(result.get(0).getQuestionId()).isEqualTo(question2.getId());
        assertThat(result.get(1).getQuestionId()).isEqualTo(question1.getId());
        assertThat(result.get(1).getAnswers().get(0).getAnswerer().getNickname()).isEqualTo(answerer.getNickname().getNickname());
    }
}