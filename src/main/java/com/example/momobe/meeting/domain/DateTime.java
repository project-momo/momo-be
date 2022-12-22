package com.example.momobe.meeting.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class DateTime {
    @Id
    @Column(name = "date_time_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Enumerated(STRING)
    @Column(nullable = false)
    private ReservationStatus reservationStatus;

    public DateTime(LocalDateTime dateTime, ReservationStatus reservationStatus) {
        this.dateTime = dateTime;
        this.reservationStatus = reservationStatus;
    }
}