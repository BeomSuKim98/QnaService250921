package com.JtoP.Spring.boundedContext.question.entity;

import com.JtoP.Spring.boundedContext.answer.entity.Answer;
import com.JtoP.Spring.boundedContext.user.entity.SiteUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@ToString
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;

    // mappedBy = "question" -> Answer 클래스의 question 필드와 매핑
    // CascadeType.REMOVE 질문이 삭제되면 그 안에 달려있는 답변도 같이 삭제 됌
    // fetch = FetchType.EAGER : 즉시 로딩을 통해 질문과 답변을 함께 가져옴
    // fetch = FetchType.LAZY : 지연 로딩을 통해 질문을 가져올 때
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    @Builder.Default // 빌더 패턴으로 객체를 생성할 때 필드 초기화
    private List<Answer> answerList = new ArrayList<>();

    public void addAnswer(Answer answer){
        answer.setQuestion(this); // Question 객체에 Answer 객체를 추가
        answerList.add(answer); // answerList에 답변 데이터 추가 이 작업을 통해 양방향 연관관계 설정

        /*
        초기화 하지 않게 되면 NullPointerException이 발생할 수 있다.
        ex) q2.getAnswerList().add(answer);
        두번 째 질문에서 리스트를 호출했지만 해당 리스트가 초기화 되어 있지 않으면 null한테 add() 를 호출함으로 인해 NullPointerException이 발생
        JPA는 클래스 내부에 있는 필드 값을 초기화 시키지 않는다.
        new ArrayList(); -> 이렇게 초기화를 해두면 컬렉션 조작이 안전하다.

        질문(Question) 입장에서 해당 질문에 달려있는 답변 목록(answerList)을 알고 있어야 한다.
        답변(Answer) 입장에서 자신이 속한 질문(Question)을 알고 있어야 한다.
        질문은 자신에게 달린 답변이 무엇인지 알고 있어야 한다.
        답변은 자신이 어떤 질문에 속해 있는지 알고 있어야 한다.
         */
    }
}