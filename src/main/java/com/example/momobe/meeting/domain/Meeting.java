package com.example.momobe.meeting.domain;

import com.example.momobe.common.domain.BaseTime;
import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.MeetingStatus;
import com.example.momobe.meeting.domain.enums.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Meeting extends BaseTime {
    @Id
    @Column(name = "meeting_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Lob
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Long hostId;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Category category;

    @ElementCollection
    @CollectionTable(name = "meeting_tag",
            joinColumns = @JoinColumn(name = "meeting_id"))
    @Enumerated(STRING)
    @Column(name = "tag", nullable = false)
    private List<Tag> tags;

    @Enumerated(STRING)
    @Column(nullable = false)
    private MeetingStatus meetingStatus;

    @Embedded
    private PriceInfo priceInfo;

    private String notice;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "meeting_id", nullable = false)
    private List<Location> locations = new ArrayList<>();

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

    public Meeting(String title, String content, Long hostId, Category category, List<Tag> tags,
                   MeetingStatus meetingStatus, PriceInfo priceInfo, String notice,
                   List<Location> locations, List<DateTime> dateTimes) {
        this.title = title;
        this.content = content;
        this.hostId = hostId;
        this.category = category;
        this.tags = tags;
        this.meetingStatus = meetingStatus;
        this.priceInfo = priceInfo;
        this.notice = notice;
        this.locations = locations;
        this.dateTimes = dateTimes;
    }
}
