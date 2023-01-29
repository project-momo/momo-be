package com.example.momobe.meeting.infarstructure;

import com.example.momobe.common.config.RedisTestConfig;
import com.example.momobe.meeting.domain.MeetingRankingStore;
import com.example.momobe.meeting.dto.out.MeetingRankDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
class MeetingRankingStoreImplTest extends RedisTestConfig {
    @Autowired
    MeetingRankingStore<MeetingRankDto> rankingStore;

    @Autowired
    RedisTemplate<String, MeetingRankDto> redisTemplate;

    private final String KEY = "KIKIKI";
    
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        rankingStore.deleteAll();
    }

    @Test
    @DisplayName("아무런 데이터도 추가되지 않은 상태에서 조회 시 결과는 empty()")
    void getRanksTest1() {
        //given
        //when
        List<MeetingRankDto> result = rankingStore.getRanks(KEY, 0, 9, MeetingRankDto.class);

        //then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("updateRank()로 데이터를 1개 삽입하고 조회 시 result size는 1")
    void getRanksTest2() {
        //given
        MeetingRankDto dto = MeetingRankDto.builder()
                .meetingId(ID1)
                .content(CONTENT1)
                .imageUrl(CONTENT1)
                .title(TITLE1)
                .build();

        rankingStore.updateRank(KEY, dto);

        //when
        List<MeetingRankDto> result = rankingStore.getRanks(KEY, 0, 9, MeetingRankDto.class);

        //then
        assertThat(result.size()).isOne();
    }

    @Test
    @DisplayName("updateRank()로 데이터 5개 삽입하고 조회 시 result size는 5")
    void getRanksTest3() {
        //given
        for (int i=0; i<5; i++) {
            MeetingRankDto dto = MeetingRankDto.builder()
                    .meetingId((long)i)
                    .content(CONTENT1)
                    .imageUrl(CONTENT1)
                    .title(TITLE1)
                    .build();

            rankingStore.updateRank(KEY, dto);
        }

        //when
        List<MeetingRankDto> result = rankingStore.getRanks(KEY, 0, 9, MeetingRankDto.class);

        //then
        assertThat(result.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("updateRank()로 데이터를 20개 삽입해도 range를 0~9까지 설정 시 result.size()는 10")
    void getRanksTest4() {
        //given
        for (int i=0; i<19; i++) {
            MeetingRankDto dto = MeetingRankDto.builder()
                    .meetingId((long)i)
                    .content(CONTENT1)
                    .imageUrl(CONTENT1)
                    .title(TITLE1)
                    .build();

            rankingStore.updateRank(KEY, dto);
        }

        //when
        List<MeetingRankDto> result = rankingStore.getRanks(KEY, 0, 9, MeetingRankDto.class);

        //then
        assertThat(result.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("랭크 조회 시, 저장 회수가 많은 (스코어가 높은) 데이터부터 순차적으로 인덱스에 배치된다")
    void getRanksTest5() {
        //given
        MeetingRankDto dto1 = MeetingRankDto.builder()
                .meetingId(ID1)
                .build();

        MeetingRankDto dto2 = MeetingRankDto.builder()
                .meetingId(ID2)
                .build();

        MeetingRankDto dto3 = MeetingRankDto.builder()
                .meetingId(ID3)
                .build();

        rankingStore.updateRank(KEY, dto1);
        rankingStore.updateRank(KEY, dto2);
        rankingStore.updateRank(KEY, dto2);
        rankingStore.updateRank(KEY, dto3);
        rankingStore.updateRank(KEY, dto3);
        rankingStore.updateRank(KEY, dto3);

        //when
        List<MeetingRankDto> result = rankingStore.getRanks(KEY, 0, 9, MeetingRankDto.class);

        //then
        System.out.println(result.size());
        assertThat(result.get(0)).isEqualTo(dto3);
        assertThat(result.get(1)).isEqualTo(dto2);
        assertThat(result.get(2)).isEqualTo(dto1);
    }

    @Test
    @DisplayName("dto 1 ~ 20을 각 수에 맞게 1~20회 저장하고 0~9로 조회했을 때, 0번째 dto는 20 9번째 dto는 11, size는 10")
    void getRanksTest6() {
        //given
        for (int i=1; i<=20; i++) {
            for (int j = 1; j <= i; j++) {
                MeetingRankDto dto = MeetingRankDto.builder()
                        .meetingId((long) i)
                        .build();

                rankingStore.updateRank(KEY, dto);
            }
        }

        //when
        List<MeetingRankDto> ranks = rankingStore.getRanks(KEY, 0, 9, MeetingRankDto.class);

        //then
        assertThat(ranks.get(0).getMeetingId()).isEqualTo(20);
        assertThat(ranks.get(9).getMeetingId()).isEqualTo(11);
        assertThat(ranks.size()).isEqualTo(10);
    }
}