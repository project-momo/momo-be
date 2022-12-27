package com.example.momobe.meeting.domain;

import com.example.momobe.common.domain.BaseTime;
import com.example.momobe.meeting.domain.enums.ReservationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Time extends BaseTime {
    @Id
    @Column(name = "time_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(STRING)
    @Column(nullable = false)
    private ReservationStatus reservationStatus;

    private LocalTime time;

    public Time(LocalTime time) {
        this.reservationStatus = ReservationStatus.UNRESERVED;
        this.time = time;
    }
}
