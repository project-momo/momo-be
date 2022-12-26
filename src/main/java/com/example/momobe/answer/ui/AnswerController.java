package com.example.momobe.answer.ui;

import com.example.momobe.answer.domain.AnswerRepository;
import com.example.momobe.answer.dto.AnswerDto;
import com.example.momobe.answer.mapper.AnswerMapper;
import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.question.dto.out.ResponseQuestionDto;
import com.example.momobe.question.infrastructure.QuestionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetings")
public class AnswerController {
    private final AnswerMapper answerMapper;
    private final AnswerRepository answerRepository;
    private final QuestionQueryRepository questionQueryRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{meetingId}/questions/{questionId}/answers")
    public List<ResponseQuestionDto> postAnswer(@PathVariable(name = "meetingId") Long meetingId,
                                                @PathVariable(name = "questionId") Long questionId,
                                                @Token UserInfo userInfo,
                                                @Valid @RequestBody AnswerDto answerDto) {
        answerRepository.save(answerMapper.of(meetingId, questionId, answerDto, userInfo));
        return questionQueryRepository.getQuestions(meetingId);
    }
}
