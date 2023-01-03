package com.example.momobe.meeting.dao;

import com.example.momobe.common.config.JpaQueryFactoryConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureDataJpa
@Import(JpaQueryFactoryConfig.class)
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MeetingDateQueryRepositoryTest {
    @Autowired
    private EntityManager em;

    private MeetingDateQueryRepository meetingDateQueryRepository;

    @BeforeEach
    void init() {
        meetingDateQueryRepository = new MeetingDateQueryRepository(new JPAQueryFactory(em));
    }

//    @Test
//    @DisplayName()
//    void
}