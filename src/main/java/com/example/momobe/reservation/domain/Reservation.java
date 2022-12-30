package com.example.momobe.reservation.domain;

import com.example.momobe.common.domain.BaseTime;
import com.example.momobe.reservation.domain.enums.ReservationState;
import lombok.*;

import javax.persistence.*;

import static com.example.momobe.reservation.domain.enums.ReservationState.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
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

    @Embedded
    private ReservationMemo reservationMemo;

    private ReservationState reservationState;

    public Reservation(ReservationDate reservationDate, Money amount, ReservedUser reservedUser, ReservationMemo reservationMemo, Long meetingId) {
        this.reservationDate = reservationDate;
        this.amount = amount;
        this.reservedUser = reservedUser;
        this.reservationMemo = reservationMemo;
        this.meetingId = meetingId;
        this.reservationState = PAYMENT_BEFORE;
    }

    public Boolean isCanceledReservation() {
        return this.reservationState.equals(CANCEL);
    }

    public Boolean checkIfPaymentFree() {
        return this.amount.getWon() == 0;
    }
}
