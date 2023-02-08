package com.example.momobe.settlement.domain;

import com.example.momobe.common.domain.BaseTime;
import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.settlement.domain.enums.SettlementState;
import com.example.momobe.settlement.domain.exception.CanNotSettleException;
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
    private Long host;
    @Column(nullable = false)
    private Long amount;
    @Column(nullable = false)
    private Long paymentId;
    @Column(nullable = false)
    private Long meetingId;

    @Column(nullable = false)
    private Long reservationId;

    @Column(nullable = false)
    private SettlementState state;

    public Settlement(Long host, Long amount, Long paymentId, Long meetingId, Long reservationId) {
        this.host = host;
        this.amount = amount;
        this.paymentId = paymentId;
        this.meetingId = meetingId;
        this.reservationId = reservationId;
        this.state  = SettlementState.WAIT;
    }
    public boolean canChangeState(Settlement settlement){
        if(settlement==null) throw new CanNotSettleException(ErrorCode.CAN_NOT_CHANGE_RESERVATION_STATE);
        return true;
    }
    public void changeSettlementState(Settlement settlement,SettlementState state){
        canChangeState(settlement);
        this.state = state;
    }
}
