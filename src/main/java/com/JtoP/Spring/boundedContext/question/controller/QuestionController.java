package com.JtoP.Spring.boundedContext.question.controller;

import com.JtoP.Spring.boundedContext.question.entity.Question;
import com.JtoP.Spring.boundedContext.question.repository.QuestionRepository;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionRepository questionRepository;

    @GetMapping("/list")
    public String showList(Model model){
        // findALL() : 모든 데이터를 조회
        // SELECT * FROM question
        List<Question> questionList = questionRepository.findAll();
        model.addAttribute("questionList", questionList);

        System.out.println("안녕하세요.");

        return "question/question_list";
    }
}