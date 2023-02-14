package com.example.momobe.common.log.repository;

import com.example.momobe.common.log.entity.ControllerLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ControllerLogRepository extends JpaRepository<ControllerLog, ControllerLogRepository> {
}
