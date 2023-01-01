package com.example.momobe.question.ui;

import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.question.domain.QuestionRepository;
import com.example.momobe.question.dto.in.QuestionDto;
import com.example.momobe.question.dto.out.ResponseQuestionDto;
import com.example.momobe.question.infrastructure.QuestionQueryRepository;
import com.example.momobe.question.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetings")
public class QuestionController {
    private final QuestionMapper questionMapper;
    private final QuestionRepository questionRepository;
    private final QuestionQueryRepository questionQueryRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{meetingsId}/questions")
    public List<ResponseQuestionDto> postQuestion(@PathVariable (name = "meetingsId") Long meetingId,
                                                  @Token UserInfo userInfo,
                                                  @Valid @RequestBody QuestionDto questionDto) {
        questionRepository.save(questionMapper.of(meetingId, userInfo, questionDto));
        return questionQueryRepository.getQuestions(meetingId);
    }

    @GetMapping("/{meetingId}/qna")
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseQuestionDto> getQuestions(@PathVariable(name = "meetingId") Long meetingId) {
        return questionQueryRepository.getQuestions(meetingId);
    }
}
