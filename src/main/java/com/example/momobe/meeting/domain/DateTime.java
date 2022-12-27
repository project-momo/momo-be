package com.example.momobe.meeting.domain;

import com.example.momobe.common.domain.BaseTime;
import com.example.momobe.meeting.domain.enums.ReservationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class DateTime extends BaseTime {
    @Id
    @Column(name = "date_time_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ElementCollection
    @CollectionTable(name = "date_time_time",
            joinColumns = @JoinColumn(name = "date_time_id"))
    @Column(name = "times", nullable = false)
    private List<LocalTime> times = new ArrayList<>();

    @Enumerated(STRING)
    @Column(nullable = false)
    private ReservationStatus reservationStatus;

    public DateTime(LocalDate date, List<LocalTime> times) {
        this.date = date;
        this.times = times;
        this.reservationStatus = ReservationStatus.UNRESERVED;
    }
}
