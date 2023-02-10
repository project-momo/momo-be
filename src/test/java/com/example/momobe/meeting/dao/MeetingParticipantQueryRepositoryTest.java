package com.example.momobe.meeting.dao;

import com.example.momobe.address.domain.Address;
import com.example.momobe.common.config.JpaQueryFactoryConfig;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.dto.out.MeetingParticipantResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.common.enums.TestConstants.REMOTE_PATH;
import static com.example.momobe.meeting.enums.MeetingConstants.generateMeeting;
import static com.example.momobe.reservation.enums.ReservationConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureDataJpa
@Import(JpaQueryFactoryConfig.class)
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MeetingParticipantQueryRepositoryTest {
    @Autowired
    private EntityManager em;

    private MeetingParticipantQueryRepository meetingParticipantQueryRepository;

    @BeforeEach
    void init() {
        meetingParticipantQueryRepository = new MeetingParticipantQueryRepository(new JPAQueryFactory(em));
    }

    @Test
    void meetingParticipantQuery() throws Exception {
        // given
        User host1 = new User(EMAIL1, NICKNAME1, PASSWORD1, new Avatar(GITHUB_URL));
        User host2 = new User(EMAIL2, NICKNAME2, PASSWORD2, new Avatar(TISTORY_URL));
        User participant = new User(EMAIL, NICKNAME, PASSWORD1, new Avatar(REMOTE_PATH));
        em.persist(host1);
        em.persist(host2);
        em.persist(participant);

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
        Meeting meeting1 = generateMeeting(host1.getId(), List.of(address1.getId(), address2.getId()));
        Meeting meeting2 = generateMeeting(host2.getId(), List.of(address1.getId(), address2.getId()));
        em.persist(meeting1);
        em.persist(meeting2);
        em.persist(generatePaymentSuccessReservation(participant.getId(), meeting1.getId()));
        em.persist(generateDenyReservation(participant.getId(), meeting2.getId()));

        // when
        Page<MeetingParticipantResponseDto> meetings =
                meetingParticipantQueryRepository.findAll(participant.getId(), PageRequest.of(0, 3));

        // then
        assertThat(meetings).isNotNull();
        assertThat(meetings.getContent()).isNotNull();
        assertThat(meetings.getContent()).hasSize(1);
    }

}
