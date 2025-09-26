package com.JtoP.Spring.boundedContext.question.controller;

import com.JtoP.Spring.boundedContext.question.entity.Question;
import com.JtoP.Spring.boundedContext.question.service.QuestionService;
import com.JtoP.Spring.boundedContext.question.input.QuestionForm;
import com.JtoP.Spring.boundedContext.answer.input.AnswerForm;

import jakarta.validation.Valid;
import org.springframework.ui.Model;

import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question/question_form";
    }

    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
        // 에러 메시지 보유 검사후 있으면 true, 없으면 false 반환
        if (bindingResult.hasErrors()) {
            return "question/question_form";
        }

        questionService.create(questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/list";
    }

    @GetMapping("/list")
    public String showList(Model model,
                           @RequestParam(defaultValue = "0") int page){
        Page<Question> paging = questionService.getList(page);
//        System.out.println("[/question/list] page=" + page
//                + ", totalElements=" + paging.getTotalElements()
//                + ", numberOfElements=" + paging.getNumberOfElements()
//                + ", isEmpty=" + paging.isEmpty());
        model.addAttribute("paging", paging);
        return "question/question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Integer id,
                         AnswerForm answerForm) {
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);

        return "question/question_detail";
    }
}