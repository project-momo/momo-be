package com.example.momobe.meeting.domain;

import com.example.momobe.common.domain.BaseTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    public DateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void init(Meeting meeting) {
        this.meeting = meeting;
    }
}
