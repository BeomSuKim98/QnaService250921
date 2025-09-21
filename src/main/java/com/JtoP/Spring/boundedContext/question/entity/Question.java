package com.JtoP.Spring.boundedContext.question.entity;

import com.JtoP.Spring.boundedContext.answer.entity.Answer;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    // mappedBy = "question" -> Answer 클래스의 question 필드와 매핑
    // CasecadeType.REMOVE 질문이 삭제되면 그 안에 달려있는 답변도 같이 삭제 됌

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
}