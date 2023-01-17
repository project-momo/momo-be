package com.example.momobe.meeting.domain;

import java.util.List;

public interface MeetingRankingStore <T> {
    List<T> getRanks(String key, int startIdx, int endIdx, Class<T> type);
    void updateRank(String key, T rankDto);
    void deleteAll();
}
