package com.example.momobe.tag.domain;

import com.example.momobe.common.config.JpaQueryFactoryConfig;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureDataJpa
@Import(JpaQueryFactoryConfig.class)
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TagRepositoryTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void init() {
        em.persist(new Tag("태그1", "tag1"));
        em.persist(new Tag("태그2", "tag2"));
    }

    @Test
    @DisplayName("존재하는 태그 2개를 조회하면 ID 2개를 반환한다.")
    void findTagIds1() {
        // given
        // when
        List<Long> tagIds = tagRepository.findTagIds(List.of("tag1", "tag2"));

        // then
        assertThat(tagIds).hasSize(2);
    }

    @Test
    @DisplayName("존재하는 태그 1개와 존재하지 않는 태그 1개를 조회하면 ID 1개를 반환한다.")
    void findTagIds2() {
        // given
        // when
        List<Long> tagIds = tagRepository.findTagIds(List.of("tag1", "tag3"));

        // then
        assertThat(tagIds).hasSize(1);
    }
}