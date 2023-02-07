package com.example.momobe.common.log.repository;

import com.example.momobe.common.log.entity.LogHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogHistoryRepository extends JpaRepository<LogHistory, LogHistoryRepository> {
}
