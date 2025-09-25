package com.JtoP.Spring.boundedContext.question.controller;

import com.JtoP.Spring.boundedContext.question.entity.Question;
import com.JtoP.Spring.boundedContext.question.repository.QuestionRepository;
import com.JtoP.Spring.boundedContext.question.service.QuestionService;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/create")
    public String questionCreate() {
        return "question/question_form";
    }

    @PostMapping("/create")
    public String questionCreate(String subject, String content) {
        if(subject == null || subject.trim().isEmpty()){
            throw new RuntimeException("제목을 입력해주세요.");
        }

        if(subject.trim().length() > 200){
            throw new RuntimeException("제목은 200자 이하로 입력해주세요.");
        }

        if(content == null || content.trim().isEmpty()){
            throw new RuntimeException("내용을 입력해주세요.");
        }

        if(content.trim().length() > 2000){
            throw new RuntimeException("내용은 2000자 이하로 입력해주세요.");
        }

        questionService.create(subject, content);
        return "redirect:/question/list";
    }

    @GetMapping("/list")
    public String showList(Model model){
        // findALL() : 모든 데이터를 조회
        // SELECT * FROM question
        List<Question> questionList = questionService.getList();
        model.addAttribute("questionList", questionList);

        return "question/question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Integer id) {
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);

        return "question/question_detail";
    }
}