package com.example.momobe.meeting.domain;

import com.example.momobe.common.domain.BaseTime;
import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.MeetingStatus;
import com.example.momobe.meeting.domain.enums.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Builder
@Entity
@Getter
@AllArgsConstructor(access = PRIVATE)
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
    private DateTimeInfo dateTimeInfo;

    @Embedded
    private PriceInfo priceInfo;

    private String notice;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "meeting_id", nullable = false)
    private List<Location> locations = new ArrayList<>();

    public Meeting(String title, String content, Long hostId, Category category, List<Tag> tags,
                   MeetingStatus meetingStatus, DateTimeInfo dateTimeInfo, PriceInfo priceInfo, String notice,
                   List<Location> locations) {
        this.title = title;
        this.content = content;
        this.hostId = hostId;
        this.category = category;
        this.tags = tags;
        this.meetingStatus = meetingStatus;
        this.dateTimeInfo = dateTimeInfo;
        this.priceInfo = priceInfo;
        this.notice = notice;
        this.locations = locations;
    }
}
