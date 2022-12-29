package com.example.momobe.meeting.dao;

import com.example.momobe.common.config.JpaQueryFactoryConfig;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.dto.MeetingHostResponseDto;
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

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.enums.MeetingConstant.generateMeeting;
import static com.example.momobe.reservation.enums.ReservationConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureDataJpa
@Import(JpaQueryFactoryConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MeetingHostQueryRepositoryTest {
    @Autowired
    private EntityManager em;

    private MeetingHostQueryRepository meetingHostQueryRepository;

    @BeforeEach
    void init() {
        meetingHostQueryRepository = new MeetingHostQueryRepository(new JPAQueryFactory(em), new MeetingQueryFactoryUtil());
    }

    @Test
    public void meetingHostQuery() throws Exception {
        // given
        User user = new User(EMAIL1, NICKNAME, PASSWORD1, new Avatar(REMOTE_PATH));
        em.persist(user);
        Meeting meeting = generateMeeting(user.getId());
        em.persist(meeting);
        em.persist(generateAcceptReservation(user.getId(), meeting.getId()));
        em.persist(generatePaymentSuccessReservation(user.getId(), meeting.getId()));
        em.persist(generateDenyReservation(user.getId(), meeting.getId()));

        // when
        Page<MeetingHostResponseDto> meetings =
                meetingHostQueryRepository.findAll(user.getId(), PageRequest.of(0, 3));

        // then
        assertThat(meetings).isNotNull();
        assertThat(meetings.getContent()).isNotNull();
        assertThat(meetings.getContent().size()).isGreaterThan(0);
    }
}