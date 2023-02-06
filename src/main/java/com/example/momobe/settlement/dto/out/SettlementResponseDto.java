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
    public static class Reservation{
        private Long paymentId;
        private Long meetingId;
        private Long reservationId;
        private Long host;
        private Long amount;

        public Reservation(Long paymentId, Long meetingId, Long reservationId, Long amount,Long host) {
            this.paymentId = paymentId;
            this.meetingId = meetingId;
            this.reservationId = reservationId;
            this.amount = amount;
            this.host = host;
        }
    }

}
