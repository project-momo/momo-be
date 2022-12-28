package com.example.momobe.meeting.dao;

import com.example.momobe.common.config.JpaQueryFactoryConfig;
import com.example.momobe.meeting.dto.MeetingResponseDto;
import com.example.momobe.user.domain.Avatar;
import com.example.momobe.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;

import static com.example.momobe.common.enums.TestConstants.REMOTE_PATH;
import static com.example.momobe.meeting.enums.MeetingConstant.generateMeeting;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureDataJpa
@Import(JpaQueryFactoryConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MeetingQueryRepositoryTest {
    @Autowired
    private EntityManager em;

    private MeetingQueryRepository meetingQueryRepository;

    @BeforeEach
    void init() {
        meetingQueryRepository = new MeetingQueryRepository(new JPAQueryFactory(em));
    }

    @Test
    public void meetingQuery() throws Exception {
        // given
        User user = (User.builder().avatar(new Avatar(REMOTE_PATH)).build());
        em.persist(user);
        em.persist(generateMeeting(user.getId()));

        // when
        Page<MeetingResponseDto> meetings =
                meetingQueryRepository.findAll(null, null, PageRequest.of(0, 3));

        // then
        assertThat(meetings).isNotNull();
        assertThat(meetings.getContent()).isNotNull();
        assertThat(meetings.getContent().size()).isGreaterThan(0);
    }

}