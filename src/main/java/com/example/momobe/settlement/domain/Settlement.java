package com.example.momobe.settlement.domain;

import com.example.momobe.common.domain.BaseTime;
import com.example.momobe.settlement.domain.emun.SettlementState;
import com.example.momobe.user.domain.UserPoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Settlement extends BaseTime {
    @Id
    @Column(name = "settlement_id")
    private Long id;

    @Embedded
    private Long amount;
    @Embedded
    private Long point;
    @Enumerated(STRING)
    private SettlementState state;
    @Embedded
    private UserPoint userPoint;
    private Long host;
    private Long payment;

    public Settlement(Long amount, UserPoint userPoint, Long host, Long payment) {
        this.amount = amount;
        this.point = userPoint.getPoint();
        this.state = SettlementState.DONE;
        this.userPoint = userPoint;
        this.host = host;
        this.payment = payment;
    }

    public SettlementState checkSettlementState(){
        return this.state;
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
