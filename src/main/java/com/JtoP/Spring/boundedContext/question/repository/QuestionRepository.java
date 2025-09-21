package com.JtoP.Spring.boundedContext.question.repository;

import com.JtoP.Spring.boundedContext.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}