package com.example.momobe.settlement.domain;

import com.example.momobe.common.domain.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Settlement extends BaseTime {
    @Id
    @Column(name = "settlement_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long amount;
    @Column(nullable = false)
    private Long host;
    @Column(nullable = false)
    private Long meeting;

    @ElementCollection
    @Column(nullable = false)
    private List<Long> reservation;

    public Settlement(Long amount, Long host, Long meeting, List<Long> reservation) {
        this.amount = amount;
        this.host = host;
        this.meeting = meeting;
        this.reservation = reservation;
    }
}


/**
 * 정산 금액
 * 정산 상태
 * 적립금
 * 정산 일시
 * =========
 * 결제 대금 전환
 * - 결제 요청 성공 시 결제 완료 페이지
 * - 결제 완료 페이지에서 api 호출
 * 1. 결제 상태 체크 -> 결제 상태에 따른 분기 처리()
 * 2. 포인트 금액 변경
 * 3.
 */
