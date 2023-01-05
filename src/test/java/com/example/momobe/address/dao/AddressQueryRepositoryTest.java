package com.example.momobe.address.dao;

import com.example.momobe.address.domain.Address;
import com.example.momobe.address.dto.AddressResponseDto;
import com.example.momobe.common.config.JpaQueryFactoryConfig;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureDataJpa
@Import(JpaQueryFactoryConfig.class)
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AddressQueryRepositoryTest {
    @Autowired
    private EntityManager em;

    private AddressQueryRepository addressQueryRepository;

    @BeforeEach
    void init() {
        addressQueryRepository = new AddressQueryRepository(new JPAQueryFactory(em));
    }

    @Test
    public void getAddresses() throws Exception {
        // given
        em.persist(Address.builder().si("서울시").gu("전체").build());
        em.persist(Address.builder().si("서울시").gu("강남구").build());
        em.persist(Address.builder().si("서울시").gu("강북구").build());

        // when
        List<AddressResponseDto> locationResponseDtos = addressQueryRepository.findAll();

        // then
        assertThat(locationResponseDtos).isNotEmpty();
        assertThat(locationResponseDtos.get(0).getSi()).isNotEmpty();
        assertThat(locationResponseDtos.get(0).getGu()).isNotEmpty();
        assertThat(locationResponseDtos.get(0).getGu().get(0).getId()).isGreaterThan(0L);
        assertThat(locationResponseDtos.get(0).getGu().get(0).getName()).isNotEmpty();
    }

}