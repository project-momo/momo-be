package com.example.momobe.settlement.domain;

import com.example.momobe.common.domain.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Settlement extends BaseTime {
    @Id
    @Column(name = "settlement_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long amount;
    @Column(nullable = false)
    private Long host;
    @Column(nullable = false)
    private Long meeting;

    @ElementCollection
    @Column(nullable = false)
    private List<Long> reservation;

    public Settlement(Long amount, Long host, Long meeting, List<Long> reservation) {
        this.amount = amount;
        this.host = host;
        this.meeting = meeting;
        this.reservation = reservation;
    }
}
