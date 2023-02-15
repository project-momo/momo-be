package com.example.momobe.reservation.dao;

import com.example.momobe.settlement.domain.enums.PointState;
import com.example.momobe.settlement.domain.enums.PointUsedType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;

@Mapper
public interface PointHistoryDao {
    @Insert("INSERT INTO point_history (current_point, history_date, request_point, state, type, user_id) " +
            "VALUES (#{currentPoint}, #{historyDate}, #{requestPoint}, #{state}, #{type}, #{userId})")
    void insertPointHistory(Long currentPoint, LocalDate date, Long requestPoint, PointState state, PointUsedType type, Long userId);
}
