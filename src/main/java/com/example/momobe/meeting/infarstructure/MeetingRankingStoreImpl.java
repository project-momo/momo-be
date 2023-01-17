package com.example.momobe.meeting.infarstructure;

import com.example.momobe.meeting.domain.MeetingRankingStore;
import com.example.momobe.user.infrastructure.UnableToProcessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.momobe.common.exception.enums.ErrorCode.UNABLE_TO_PROCESS;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MeetingRankingStoreImpl<T> implements MeetingRankingStore<T> {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private ZSetOperations<String, String> zSetOperations;
    private final String KEY = "RANKING";

    @PostConstruct
    public void init() {
        zSetOperations = redisTemplate.opsForZSet();
    }

    @Override
    public List<T> getRanks(String key, int startIdx, int endIdx, Class<T> type) {
        Set<String> jsonSet = zSetOperations.reverseRange(KEY, startIdx, endIdx);
        return convertToValues(jsonSet, type);
    }

    @Override
    public void updateRank(String key, T rankDto) {
        String json = convertToJson(rankDto);

        zSetOperations.addIfAbsent(KEY, json, 0);
        zSetOperations.incrementScore(KEY, json, 1);
    }

    @Override
    public void deleteAll() {
        Long size = zSetOperations.size(KEY);
        zSetOperations.removeRange(KEY, 0, size);
    }

    private String convertToJson(T rankDto) {
        try {
            return objectMapper.writeValueAsString(rankDto);
        } catch (Exception exception) {
            log.error("Redis IOException = ", exception);
            throw new UnableToProcessException(UNABLE_TO_PROCESS);
        }
    }

    private List<T> convertToValues(Set<String> jsonSet, Class<T> type) {
        return jsonSet.stream()
                .map(e -> {
                    T value = null;
                    try {
                        value = objectMapper.readValue(e, type);
                    } catch (Exception exception) {
                        log.error("Redis IOException = ", exception);
                        throw new UnableToProcessException(UNABLE_TO_PROCESS);
                    }
                    return value;
                }).collect(Collectors.toList());
    }
}