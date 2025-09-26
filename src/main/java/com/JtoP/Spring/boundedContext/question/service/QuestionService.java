package com.JtoP.Spring.boundedContext.question.service;

import com.JtoP.Spring.boundedContext.question.entity.Question;
import com.JtoP.Spring.boundedContext.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import com.JtoP.Spring.global.exception.DataNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public Page<Question> getList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return questionRepository.findAll(pageable);
    }

    public Question getQuestion(Integer id){
        // SELECT * FROM question WHERE id = ?
        Optional<Question> question = questionRepository.findById(id);
        if (question.isPresent()){
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content) {
        Question q = Question.builder()
                .subject(subject)
                .content(content)
                .createDate(LocalDateTime.now())
                .build();
        questionRepository.save(q);
    }
}


