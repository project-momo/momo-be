package com.example.momobe.meeting.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Meeting {
    @Id
    @Column(name = "meeting_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Long hostId;
    @Column(nullable = false)
    private Long categoryId;

    @ElementCollection
    @CollectionTable(name = "meeting_tag",
            joinColumns = @JoinColumn(name = "meeting_id"))
    @Column(name = "tag_id", nullable = false)
    private Set<Long> tagIds;

    @Enumerated(STRING)
    @Column(nullable = false)
    private MeetingStatus meetingStatus;

    @Embedded
    private PriceInfo priceInfo;

    private String notice;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "meeting_id", nullable = false)
    private List<Location> locations = new ArrayList<>();

    public Meeting(String title, String content, Long hostId, Long categoryId, Set<Long> tagIds,
                   MeetingStatus meetingStatus, PriceInfo priceInfo, String notice, List<Location> locations) {
        this.title = title;
        this.content = content;
        this.hostId = hostId;
        this.categoryId = categoryId;
        this.tagIds = tagIds;
        this.meetingStatus = meetingStatus;
        this.priceInfo = priceInfo;
        this.notice = notice;
        this.locations = locations;
    }
}
