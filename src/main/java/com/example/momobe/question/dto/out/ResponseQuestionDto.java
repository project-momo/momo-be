package com.example.momobe.question.dto.out;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class ResponseQuestionDto {
    private final Long questionId;
    private final String content;
    private final User questioner;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final List<Answer> answers;

    @QueryProjection
    public ResponseQuestionDto(Long questionId, String content, Long userId, String email, String nickname, String imageUrl, LocalDateTime createdAt, LocalDateTime modifiedAt, List<Answer> answers) {
        this.questionId = questionId;
        this.content = content;
        this.questioner = new User(userId, email, nickname, imageUrl);
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.answers = answers;
    }

    @Builder
    public ResponseQuestionDto(Long questionId, String content, User questioner, LocalDateTime createdAt, LocalDateTime modifiedAt, List<Answer> answers) {
        this.questionId = questionId;
        this.content = content;
        this.questioner = questioner;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.answers = answers;
    }

    @Getter
    @ToString
    public static class Answer {
        private final Long answerId;
        private final String content;
        private final User answerer;
        private final LocalDateTime createdAt;
        private final LocalDateTime modifiedAt;

        @QueryProjection
        public Answer(Long answerId, String content, Long userId, String email, String nickname, String imageUrl, LocalDateTime createdAt, LocalDateTime modifiedAt) {
            this.answerId = answerId;
            this.content = content;
            this.answerer = new User(userId, email, nickname, imageUrl);;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
        }

        @Builder
        public Answer(Long answerId, String content, User answerer, LocalDateTime createdAt, LocalDateTime modifiedAt) {
            this.answerId = answerId;
            this.content = content;
            this.answerer = answerer;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
        }
    }

    @Builder
    @Getter
    @ToString
    public static class User {
        private final Long userId;
        private final String email;
        private final String nickname;
        private final String imageUrl;

        public User(Long userId, String email, String nickname, String imageUrl) {
            this.userId = userId;
            this.email = email;
            this.nickname = nickname;
            this.imageUrl = imageUrl;
        }
    }
}
