package com.example.momobe.meeting.domain;

import com.example.momobe.common.domain.BaseTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Location extends BaseTime {
    @Id
    @Column(name = "location_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    public Location(Address address) {
        this.address = address;
    }
}
