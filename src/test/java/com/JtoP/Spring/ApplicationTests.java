package com.JtoP.Spring;

import com.JtoP.Spring.boundedContext.answer.entity.Answer;
import com.JtoP.Spring.boundedContext.answer.repository.AnswerRepository;
import com.JtoP.Spring.boundedContext.answer.service.AnswerService;
import com.JtoP.Spring.boundedContext.question.entity.Question;
import com.JtoP.Spring.boundedContext.question.repository.QuestionRepository;
import com.JtoP.Spring.boundedContext.user.entity.SiteUser;
import com.JtoP.Spring.boundedContext.user.repository.UserRepository;
import com.JtoP.Spring.boundedContext.user.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import com.JtoP.Spring.boundedContext.question.service.QuestionService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("test")
class ApplicationTests {

    @Autowired javax.sql.DataSource ds;

    @Test
    void whereAmI() throws Exception {
        try (var c = ds.getConnection()) {
            System.out.println("URL = " + c.getMetaData().getURL());
            try (var rs = c.createStatement().executeQuery("SELECT DATABASE()")) {
                rs.next();
                System.out.println("DB  = " + rs.getString(1));
            }
        }
    }


    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AnswerService answerService;

    @BeforeEach
        // 각 테스트 메서드가 실행되기 전에 실행되는 메서드
    void beforeEach() {
        // 답변 데이터 삭제
        answerRepository.deleteAll();
        answerRepository.clearAutoIncrement(); // answer 테이블의 AUTO_INCREMENT 초기화

        // 질문 데이터 삭제
        questionRepository.deleteAll();
        questionRepository.clearAutoIncrement(); // question 테이블의 AUTO_INCREMENT 초기화

        userRepository.deleteAll();
        userRepository.clearAutoIncrement(); // user 테이블의 AUTO_INCREMENT 초기화

        SiteUser user1 = userService.create("user1", "user1@test.com", "1234");
        SiteUser user2 = userService.create("user2", "user2@test.com", "1234");

        Question q1 = questionService.create("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.", user1);
        Question q2 = questionService.create("스프링부트 모델 질문입니다.", "id는 자동으로 생성되나요?", user2);
        // 답변 데이터 생성
        Answer a1 = answerService.create(q2, "네 자동으로 생성됩니다.", user1);
        q2.addAnswer(a1); // 양방향 연관관계 설정
        a1.setCreateDate(LocalDateTime.now());
    }

    @Test
    @DisplayName("데이터저장")
    void t1() {
        SiteUser user1 = userService.getUser("user1");
        questionService.create("스프링부트 학습은 어떻게 해야 하나요?", "스프링부트 학습은 처음입니다.", user1);
        questionService.create("자바를 이용한 객체 지향 설계는 어떻게 하나요?", "자바 객체지향 설계 방법을 모르겠습니다.", user1);
    }

    @Test
    @DisplayName("findAll")
    void t2() {
        // SELECT * FROM question;
        List<Question> all = questionRepository.findAll();
        assertEquals(2, all.size());

        Question q = all.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());
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

    @Test
    @DisplayName("데이터 삭제")
    void t8(){
        // SELECT COUNT(*) FROM question
        //count() : 테이블에 있는 데이터의 개수를 반환
        assertEquals(2, questionRepository.count());
        Optional<Question> oq = questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        questionRepository.delete(q);
        assertEquals(1, questionRepository.count());
    }

    // 질문 데이터 가졍오기
    /*
    SELCET * FROM question AS q1 WHERE q1.id = ?
     */

    // 특정 질문에 대한 답변 추가
    /*
    INSERT INTO answer (content, create_date, question_id) VALUES (?, ?, ?)
    또는
    INSERT INTO answer
    SET content = ?,
        create_date = ?,
        question_id = ?
     */

    @Test
    @DisplayName("답변 데이터 생성 후 저장")
    void t9(){
        // v1
        Optional<Question> oq = questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        // v2
        // Question q = questionRepository.findById(2).get();

        SiteUser user2 = userService.getUser("user2");
        Answer a = answerService.create(q, "네 자동으로 생성됩니다.", user2);
        assertEquals("네 자동으로 생성됩니다.", a.getContent());
    }

    @Test
    @DisplayName("답변 데이터 조회")
    void t10() {
        // 답변 데이터 조회
        Optional<Answer> oa = answerRepository.findById(1);
        assertTrue(oa.isPresent());
        Answer a = oa.get();
        assertEquals(2, a.getQuestion().getId());
    }

    @Test
    @Transactional // 테스트 환경에서는 트랙잭션 없이 DB로 연결이 이어지지 않는다.
    @DisplayName("질문 데이터로 답변 데이터 조회")
    @Rollback(false) // 테스트 메서드가 끝난 후 트랜잭션을 롤백하지 않음, 실제 DB에 반영
    void t11() {
        // SELECT * FROM question WHERE id = 2
        Optional<Question> oq = questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question q = oq.get(); // get이후에는 DB 연결을 끓음
        // 질문 객체 생성 후 getAnswerList() 메서드를 통해 해당 질문에 달린 모든 답변을 가져옴

        List<Answer> answerList = q.getAnswerList();
        assertEquals(1, answerList.size());
        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
    }

    @Test
    @DisplayName("대량 데이터 삽입")
    void t12() {
        SiteUser user2 = userService.getUser("user2");

        IntStream.rangeClosed(3, 300).forEach(i ->
            questionService.create(
           "테스트 제목입니다. %d".formatted(i),
           "테스트 내용입니다. %d".formatted(i), user2));
    }
}
