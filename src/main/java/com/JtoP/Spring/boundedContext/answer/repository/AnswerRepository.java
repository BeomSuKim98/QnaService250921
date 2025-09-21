package com.JtoP.Spring.boundedContext.answer.repository;

import com.JtoP.Spring.boundedContext.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}