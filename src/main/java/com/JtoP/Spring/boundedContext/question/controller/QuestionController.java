package com.JtoP.Spring.boundedContext.question.controller;

import com.JtoP.Spring.boundedContext.question.entity.Question;
import com.JtoP.Spring.boundedContext.question.service.QuestionService;
import com.JtoP.Spring.boundedContext.question.input.QuestionForm;
import com.JtoP.Spring.boundedContext.answer.input.AnswerForm;

import com.JtoP.Spring.boundedContext.user.entity.SiteUser;
import com.JtoP.Spring.boundedContext.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {
    private final UserService userService;
    private final QuestionService questionService;

    @PreAuthorize("isAuthenticated()")
    // 메서드 단위 권한 검사를 수행하는 애너테이션
    // 현재 인증된 사용자가 ADMIN 권한을 가지고 있을 때만 해당 메서드를 실행할 수 있도록 제한
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question/question_form";
    }

    @PostMapping("/create")
    public String questionCreate(
            @Valid QuestionForm questionForm,
            BindingResult bindingResult,
            Principal principal) {

        SiteUser siteUser =
                userService.getUser(principal.getName());

        questionService.create(questionForm.getSubject(),
                questionForm.getContent(),
                siteUser);
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm,
                                 @PathVariable("id") Integer id,
                                 Principal principal) {
        Question question = questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());

        return "question/question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult,
                                 @PathVariable("id") Integer id,
                                 Principal principal){
        if(bindingResult.hasErrors()){
            return "question/question_form";
        }

        Question question = questionService.getQuestion(id);

        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        questionService.modify(question,
                questionForm.getSubject(),
                questionForm.getContent());
        return "redirect:/question/detail/%s".formatted(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal,
                                 @PathVariable("id") Integer id){
        Question question = questionService.getQuestion(id);

        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        questionService.delete(question);
        return "redirect:/";
    }
}