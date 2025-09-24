package com.JtoP.Spring.boundedContext.answer.service;

import com.JtoP.Spring.boundedContext.answer.entity.Answer;
import com.JtoP.Spring.boundedContext.answer.repository.AnswerRepository;
import com.JtoP.Spring.boundedContext.question.entity.Question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public Answer create(Question question, String content){
        Answer answer = Answer.builder()
                .content(content)
                .createDate(LocalDateTime.now())
                .question(question)
                .build();

        answerRepository.save(answer);
        return answer;
    }
}
