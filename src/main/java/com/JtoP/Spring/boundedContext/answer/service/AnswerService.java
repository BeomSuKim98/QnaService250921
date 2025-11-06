package com.JtoP.Spring.boundedContext.answer.service;

import com.JtoP.Spring.boundedContext.answer.entity.Answer;
import com.JtoP.Spring.boundedContext.answer.repository.AnswerRepository;
import com.JtoP.Spring.boundedContext.question.entity.Question;

import com.JtoP.Spring.boundedContext.user.entity.SiteUser;
import com.JtoP.Spring.global.exception.DataNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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

    public Answer create (Question question, String content, SiteUser author){
        Answer answer = Answer.builder()
                .content(content)
                .createDate(LocalDateTime.now())
                .question(question)
                .author(author)
                .build();

        answerRepository.save(answer);
        return answer;
    }

    public Answer getAnswer(Integer id){
        Optional<Answer> answer = answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("사용자를 찾을 수 없습니다.");
        }
    }

    public void modify(Answer answer, String content){
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        answerRepository.save(answer);
    }

    public void delete(Answer answer){
        answerRepository.delete(answer);
    }

    public void vote(Answer answer, SiteUser siteUser){
        answer.getVoter().add(siteUser);
        answerRepository.save(answer);
    }
}
