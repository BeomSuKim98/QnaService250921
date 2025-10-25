package com.JtoP.Spring.global.base.initData;

import com.JtoP.Spring.boundedContext.question.entity.Question;
import com.JtoP.Spring.boundedContext.question.service.QuestionService;
import com.JtoP.Spring.boundedContext.user.entity.SiteUser;
import com.JtoP.Spring.boundedContext.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

public  class NotProd {
    @Bean
    CommandLineRunner initData(QuestionService questionService,
                               UserService userService){
        return args -> {
            SiteUser user1 = userService.create("user1",
                    "user1@test.com", "1234");
            SiteUser user2 = userService.create("user2",
                    "user2@test.com", "1234");

            Question q1 = questionService.create("질문1 제목입니다.",
                    "질문1 내용입니다.", user1);
            Question q2 = questionService.create("질문2 제목입니다.",
                    "질문2 내용입니다.", user2);
        };
    }
}