package com.example.momobe.common.log.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class ControllerLog {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String operatorId;

    private String operatorIP;

    private String requestUri;

    private String httpMethod;

    private LocalDateTime createdAt;
}
