package com.example.momobe.common.log.service;

import com.example.momobe.common.log.entity.LogHistory;
import com.example.momobe.common.log.repository.LogHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogHistoryService {
    private final LogHistoryRepository logHistoryRepository;

    public void saveLog(LogHistory log) {
        logHistoryRepository.save(log);
    }
}
