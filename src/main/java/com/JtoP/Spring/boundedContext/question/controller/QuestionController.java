package com.JtoP.Spring.boundedContext.question.controller;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/question")
public class QuestionController {
    @GetMapping("/list")
    public String showList(Model model){
        model.addAttribute("age,20");
        return "question/question_list";
    }
}