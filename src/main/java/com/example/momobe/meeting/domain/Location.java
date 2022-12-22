package com.example.momobe.meeting.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Location {
    @Id
    @Column(name = "location_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "location_id", nullable = false)
    private List<DateTime> dateTimes;

    public Location(Address address, List<DateTime> dateTimes) {
        this.address = address;
        this.dateTimes = dateTimes;
    }
}
