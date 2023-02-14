package com.example.momobe.common.log.service;

import com.example.momobe.common.log.entity.ExceptionLog;
import com.example.momobe.common.log.repository.ExceptionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExceptionLogService {
    private final ExceptionLogRepository exceptionLogRepository;

    public void save(ExceptionLog log) {
        exceptionLogRepository.save(log);
    }
}
