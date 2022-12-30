package com.example.momobe.meeting.domain;

import com.example.momobe.common.domain.BaseTime;
import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.example.momobe.meeting.domain.enums.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Column(nullable = false)
    private Integer personnel;

    @Enumerated(STRING)
    @Column(nullable = false)
    private MeetingState meetingState;

    @Embedded
    private DateTimeInfo dateTimeInfo;

    @Column(nullable = false)
    private Long price;

    private String notice;

    @Embedded
    private Address address;

    @Builder
    public Meeting(String title, String content, Long hostId, Category category, List<Tag> tags,
                   Integer personnel, MeetingState meetingState, DateTimeInfo dateTimeInfo,
                   Long price, String notice, Address address) {
        this.title = title;
        this.content = content;
        this.hostId = hostId;
        this.category = category;
        this.tags = tags;
        this.personnel = personnel;
        this.meetingState = meetingState;
        this.dateTimeInfo = dateTimeInfo;
        dateTimeInfo.getDateTimes().forEach(dateTime -> dateTime.init(this));
        this.price = price;
        this.notice = notice;
        this.address = address;
    }

    public Long calculateRemainingQuota(Long quota) {
        return this.personnel - quota;
    }
}
