package com.example.momobe.question.domain;

import com.example.momobe.common.domain.BaseTime;
import com.example.momobe.question.domain.enums.QuestionStateType;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTime {
    @Id
    @Column(name = "question_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Embedded
    private Meeting meeting;

    @Embedded
    private Writer writer;

    @Embedded
    private Content content;

    @Enumerated(EnumType.STRING)
    private QuestionStateType questionState;

    public Question(Long meetingId, String content, Long writerId) {
        this.meeting = new Meeting(meetingId);
        this.content = new Content(content);
        this.writer = new Writer(writerId);
        this.questionState = QuestionStateType.UNANSWERED;
    }
}
