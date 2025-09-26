package com.JtoP.Spring.boundedContext.question.repository;

import com.JtoP.Spring.boundedContext.question.entity.Question;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findBySubject(String subject);

    Question findBySubjectAndContent(String subject, String content);

    List<Question> findBySubjectLike(String keyword);

    //    페이징 된 데이터를 반환
    Page<Question> findAll(Pageable pageable);

    @Modifying // Query는 기본적으로 SELECT 문에 사용되므로
    // INSERT, UPDATE, DELETE 같은 변경 작업을 수행할 때는 @Modifying 어노테이션이 필요
    @Transactional // 데이터베이스의 상태를 변경하는 작업이므로 트랜잭션 관리가 필요
    // 트랜잭션이란 데이터베이스의 상태를 변화시키는 하나의 작업 단위
    // 트랜잭션 내의 작업들은 모두 성공하거나 모두 실패해야 함, 성공시 커밋, 실패시 롤백
    @Query(value = "ALTER TABLE question AUTO_INCREMENT = 1", nativeQuery = true)
    // Query 어노테이션을 사용하여 네이티브 SQL 쿼리를 실행
    // AUTO_INCREMENT 값을 1로 재설정, nativeQuery = true -> SQL 문법을 직접 사용
    void clearAutoIncrement();
    // JPA에서 자동 생성되는 PK를 강제로 되돌리기 때문에 무결성 문제 발생 가능, 테스트 코드에서만 사용

}