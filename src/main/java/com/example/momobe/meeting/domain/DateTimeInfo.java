package com.example.momobe.meeting.domain;

import com.example.momobe.meeting.domain.enums.DatePolicy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DateTimeInfo {
    @Column(nullable = false)
    private DatePolicy datePolicy;

    @Column(nullable = false)
    private LocalDate startDate;
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalTime startTime;
    @Column(nullable = false)
    private LocalTime endTime;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "meeting_id", nullable = false)
    private List<DateTime> dateTimes = new ArrayList<>();
}
