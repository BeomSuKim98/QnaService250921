package com.JtoP.Spring;

import com.JtoP.Spring.boundedContext.question.entity.Question;
import com.JtoP.Spring.boundedContext.question.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("test")
class ApplicationTests {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("데이터 저장")
        // 사용자가 읽을 수 있게 표기
    void t1() {
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        questionRepository.save(q1); // 첫 번째 질문 저장
    }

    void t2(){
        Question q2 = Question.builder()
                .subject("스프링부트 모델 질문입니다.")
                .content("id는 자동으로 생성되나요?")
                .createDate(LocalDateTime.now())
                .build();

        questionRepository.save(q2); // 두 번째 질문 저장, 빌더를 이용한 저장 방법
    }

    @Test
    @DisplayName("findById")
    void t3() {
        // SELECT * FROM question WHERE id = 1 와 같은 의미
        // Optional : null이 될 수도 있는 객체를 감싸는 래퍼 클래스(컨테이너)
        Optional<Question> oq = questionRepository.findById(1);
        if(oq.isPresent()){
            Question q = oq.get();
            assertEquals("sbb가 무엇인가요?", q.getSubject());
        }
    }

    @Test
    @DisplayName("findBySubject")
    void t4(){
        // SELECT * FROM question WHERE subject = 'sbb가 무엇인가요?' 와 같은 의미

        Question q = questionRepository.findBySubject("sbb가 무엇인가요?");
        assertEquals(1, q.getId());
    }

    @Test
    @DisplayName("findBySubjectAndContent")
    void t5(){
        // SELECT * FROM question WHERE subject = 'sbb가 무엇인가요?' AND content = 'sbb에 대해서 알고 싶습니다.' 와 같은 의미
        Question q = questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
        assertEquals(1, q.getId());
    }


    // SELECT * FROM question WHERE subject LIKE 'sbb%'
    @Test
    @DisplayName("findBySubjectLike")
    void t6(){
        List<Question> qList = questionRepository.findBySubjectLike("sbb%");
        Question q = qList.get(0); // 위 List로 불러온 결과 값 중 첫 번째 값을 가져옴
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    @Test
    @DisplayName("데이터 수정")
    void t7(){
        // SELECT * FROM question WHERE id = 1
        Optional<Question> oq = questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        q.setSubject("수정된 제목");
        questionRepository.save(q);
    }

}
