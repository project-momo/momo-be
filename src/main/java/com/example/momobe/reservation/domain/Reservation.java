package com.example.momobe.reservation.domain;

import com.example.momobe.common.domain.BaseTime;
import com.example.momobe.reservation.domain.enums.ReservationState;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reservation extends BaseTime {
    @Id
    @Column(name = "reservation_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long meetingId;

    @Embedded
    private ReservationDate reservationDate;

    @Embedded
    private Money amount;

    @Embedded
    private ReservedUser reservedUser;

    //TODO : 예약자가 남기는 메모를 의미한다. 로직 완성 후 주석은 삭제할 것
    @Embedded
    private ReservationMemo reservationMemo;

    private ReservationState reservationState;

    public Reservation(ReservationDate reservationDate, Money amount, ReservedUser reservedUser, ReservationMemo reservationMemo, ReservationState reservationState, Long meetingId) {
        this.reservationDate = reservationDate;
        this.amount = amount;
        this.reservedUser = reservedUser;
        this.reservationMemo = reservationMemo;
        this.reservationState = reservationState;
        this.meetingId = meetingId;
    }

    public boolean checkIfCanceledReservation() {
        return this.reservationState.equals(ReservationState.CANCEL);
    }
}
