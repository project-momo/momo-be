package com.example.momobe.settlement.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointHistoryFindRepository extends JpaRepository<PointHistory,Long> {

    @Query(nativeQuery = true, value = "SELECT * "+ "FROM point_history "+ "where user_Id = :user_id")
    Page<PointHistory> findAllByUser_UserId(@Param("user_id") Long id, Pageable pageable);
}
