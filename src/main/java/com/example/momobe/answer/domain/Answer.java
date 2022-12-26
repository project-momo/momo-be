package com.example.momobe.answer.domain;

import com.example.momobe.common.domain.BaseTime;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @Embedded
    private Content content;

    @Embedded
    private Meeting meeting;

    @Embedded
    private Writer writer;

    @Embedded
    private Question question;

    public Answer(Content content, Meeting meeting, Writer writer, Question question) {
        this.content = content;
        this.meeting = meeting;
        this.writer = writer;
        this.question = question;
    }
}
