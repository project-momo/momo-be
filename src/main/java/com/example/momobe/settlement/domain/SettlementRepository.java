package com.example.momobe.settlement.domain;

import com.example.momobe.settlement.domain.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement,Long> {
    Settlement findByReservationId(Long id);
}
