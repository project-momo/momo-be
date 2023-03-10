package com.example.momobe.meeting.domain;

import com.example.momobe.common.domain.BaseTime;
import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.MeetingState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    @Column(name = "tag_id", nullable = false)
    private List<Long> tagIds;

    @Column(nullable = false)
    private Integer personnel;

    @Enumerated(STRING)
    @Column(nullable = false)
    private MeetingState meetingState;

    @Embedded
    private DateTimeInfo dateTimeInfo;

    @Column(nullable = false)
    private Long price;

    @Embedded
    private Address address;

    @Builder
    public Meeting(String title, String content, Long hostId, Category category, List<Long> tagIds,
                   Integer personnel, MeetingState meetingState, DateTimeInfo dateTimeInfo,
                   Long price, Address address) {
        this.title = title;
        this.content = content;
        this.hostId = hostId;
        this.category = category;
        this.tagIds = tagIds;
        this.personnel = personnel;
        this.meetingState = meetingState;
        this.dateTimeInfo = dateTimeInfo;
        Optional.ofNullable(dateTimeInfo).ifPresent(
                dateTimeInfo1 -> dateTimeInfo.getDateTimes().forEach(dateTime -> dateTime.init(this)));
        this.price = price;
        this.address = address;
    }

    public Boolean hasRemainingReservations(Long currentAttendees) {
        return this.personnel - currentAttendees > 0;
    }

    public Boolean isValidReservationSchedule(LocalDate date,
                                              LocalTime startTime,
                                              LocalTime endTime) {
        return dateTimeInfo.match(date, startTime, endTime);
    }

    public Boolean matchPrice(Long price,
                              LocalTime startTime,
                              LocalTime endTime) {
        if (this.dateTimeInfo.hasFreePolish()) {
            int period = endTime.getHour() - startTime.getHour();
            return Objects.equals(period * this.price, price);
        }

        return Objects.equals(this.price, price);
    }

    public Boolean isClosed() {
        return this.meetingState == MeetingState.CLOSE;
    }

    public void verifyHostId(Long hostId) {
        if (!this.getHostId().equals(hostId)) {
            throw new MeetingException(ErrorCode.REQUEST_DENIED);
        }
    }

    public void closeWithHostId(Long hostId) {
        verifyHostId(hostId);
        this.meetingState = MeetingState.CLOSE;
    }

    public Boolean matchHostId(Long hostId) {
        return Objects.equals(this.hostId, hostId);
    }

    public void updateMeetingInfo(Meeting meeting) {
        verifyHostId(meeting.getHostId());
        Optional.ofNullable(meeting.getTitle()).ifPresent(t -> this.title = t);
        Optional.ofNullable(meeting.getContent()).ifPresent(c -> this.content = c);
        Optional.ofNullable(meeting.getCategory()).ifPresent(c -> this.category = c);
        Optional.ofNullable(meeting.getTagIds()).ifPresent(ts -> this.tagIds = ts);
        Optional.ofNullable(meeting.getPersonnel()).ifPresent(p -> this.personnel = p);
        Optional.ofNullable(meeting.getPrice()).ifPresent(p -> this.price = p);
        Optional.ofNullable(meeting.getAddress()).ifPresent(a -> this.address = a);
    }
}
