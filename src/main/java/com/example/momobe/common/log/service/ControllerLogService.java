package com.example.momobe.common.log.service;

import com.example.momobe.common.log.entity.ControllerLog;
import com.example.momobe.common.log.repository.ControllerLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ControllerLogService {
    private final ControllerLogRepository controllerLogRepository;

    public void saveLog(ControllerLog log) {
        controllerLogRepository.save(log);
    }
}
