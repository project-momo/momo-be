package com.example.momobe.settlement.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiAccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false)
    private Long seq;

    @Setter
    @Column(nullable = false,length = 400)
    private String assessToken;

    @Column(nullable = false)
    String tokenType;

    @Column(nullable = false)
    String expireDate;

    @Column(nullable = false)
    String scope;

    @Column(nullable = false)
    String clientUseCode;

}
