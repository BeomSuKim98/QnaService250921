package com.JtoP.Spring.boundedContext.answer.controller;

import com.JtoP.Spring.boundedContext.answer.entity.Answer;
import com.JtoP.Spring.boundedContext.answer.service.AnswerService;
import com.JtoP.Spring.boundedContext.question.entity.Question;
import com.JtoP.Spring.boundedContext.question.service.QuestionService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/create/{id}")
    public String createAnswer(Model model,
                               @PathVariable("id") Integer id,
                               @RequestParam(value="content") String content) {
        // pathVariable("id") : URL 경로에 포함된 변수를 메서드 매개변수로 전달
        // requestParam("content") : 폼 데이터로 전달된 변수를 메서드 매개변수로 전달
        Question question = questionService.getQuestion(id);
        // id에 해당하는 질문을 데이터베이스에서 조회

        Answer answer = answerService.create(question, content);

        return String.format("redirect:/question/detail/%s", id);
        // 저장이 끝나고 리다이렉트: 브라우저에게 새로운 URL로 이동하라고 지시
    }
}