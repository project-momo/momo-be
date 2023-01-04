package com.example.momobe.meeting.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@Embeddable
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Address {

    @ElementCollection
    @CollectionTable(name = "meeting_address",
            joinColumns = @JoinColumn(name = "meeting_id"))
    @Column(name = "address_id", nullable = false)
    private List<Long> addressIds;
    private String addressInfo;
}
