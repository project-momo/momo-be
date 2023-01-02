package com.example.momobe.meeting.domain;

import com.example.momobe.meeting.domain.enums.DatePolicy;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.momobe.meeting.domain.enums.DatePolicy.*;
import static javax.persistence.EnumType.STRING;

@Getter
@Builder
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DateTimeInfo {
    @Enumerated(STRING)
    @Column(nullable = false)
    private DatePolicy datePolicy;

    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalTime startTime;
    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private Integer maxTime;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DateTime> dateTimes = new ArrayList<>();

    protected Boolean match(LocalDate date,
                         LocalTime startTime,
                         LocalTime endTime) {
        if (this.datePolicy.equals(FREE)) {
            return dateMatch(date) && startTimeMatch(startTime) && endTimeMatch(endTime) && maxTimeMatch(startTime, endTime);
        } else {
            return dateMatch(date) && startTime == this.startTime && endTime == this.endTime;
        }
    }

    private Boolean dateMatch(LocalDate date) {
        return (date.toEpochDay() >= this.startDate.toEpochDay()) && (date.toEpochDay() <= this.endDate.toEpochDay());
    }

    private Boolean startTimeMatch(LocalTime startTime) {
        return (startTime.getSecond() >= this.startTime.getSecond()) && (startTime.getSecond() <= this.endTime.getSecond());
    }

    private Boolean endTimeMatch(LocalTime endTime) {
        return (endTime.getSecond() >= this.startTime.getSecond()) && (endTime.getSecond() <= this.endTime.getSecond());
    }

    private Boolean maxTimeMatch(LocalTime startTime,
                                 LocalTime endTime) {
        return endTime.getSecond() - startTime.getSecond() < maxTime;
    }

    public Boolean hasFreePolish() {
        return this.datePolicy == FREE;
    }
}
