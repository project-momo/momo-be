package com.example.momobe.answer.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {
    private String content;
}
