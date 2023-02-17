package com.example.momobe.settlement.dto.out;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class SettlementResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    public static class SettlementDto{
        private Long paymentId;
        private Long meetingId;
        private Long reservationId;
        private Long host;
        private Long amount;

        public SettlementDto(Long paymentId, Long meetingId, Long reservationId,Long host, Long amount) {
            this.paymentId = paymentId;
            this.meetingId = meetingId;
            this.reservationId = reservationId;
            this.host = host;
            this.amount = amount;
        }
    }

}
