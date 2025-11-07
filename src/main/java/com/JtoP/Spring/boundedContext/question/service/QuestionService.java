package com.JtoP.Spring.boundedContext.question.service;

import com.JtoP.Spring.boundedContext.question.entity.Question;
import com.JtoP.Spring.boundedContext.question.repository.QuestionRepository;
import com.JtoP.Spring.boundedContext.user.entity.SiteUser;
import com.JtoP.Spring.boundedContext.answer.entity.Answer;

import com.JtoP.Spring.global.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;

import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        // SELECT * FROM question ORDER BY create_date DESC LIMIT 0, 10
        Specification<Question> spec = search(kw);

        return questionRepository.findAll(spec, pageable);
    }

    private Specification<Question> search(String kw) {
        return (root, query, cb) -> {
            query.distinct(true); // JOIN으로 생긴 중복 제거

            // 조인들
            Join<Question, SiteUser> qAuthor = root.join("author", JoinType.LEFT);
            Join<Question, Answer> answers = root.join("answerList", JoinType.LEFT);
            Join<Answer, SiteUser> aAuthor = answers.join("author", JoinType.LEFT);

            // 대소문자 무시 검색
            String like = "%" + (kw == null ? "" : kw.trim().toLowerCase()) + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("subject")), like),          // 질문 제목
                    cb.like(cb.lower(root.get("content")), like),          // 질문 내용
                    cb.like(cb.lower(qAuthor.get("username")), like),      // 질문 작성자
                    cb.like(cb.lower(answers.get("content")), like),       // 답변 내용
                    cb.like(cb.lower(aAuthor.get("username")), like)       // 답변 작성자
            );
        };
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

    public Question create(String subject, String content) {
        Question q = Question.builder()
                .subject(subject)
                .content(content)
                .createDate(LocalDateTime.now())
                .build();
        questionRepository.save(q);

        return q;
    }

    public Question create(String subject, String content, SiteUser author){
        Question q = Question.builder()
                .subject(subject)
                .content(content)
                .createDate(LocalDateTime.now())
                .author(author)
                .build();
        questionRepository.save(q);

        return q;
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        questionRepository.save(question);
    }

    public void delete(Question question) {
        questionRepository.delete(question);
    }

    public void vote(Question question, SiteUser siteUser){
        question.getVoter().add(siteUser);
        questionRepository.save(question);
    }
}


