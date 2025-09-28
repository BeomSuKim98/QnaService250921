package com.JtoP.Spring.boundedContext.user.entity;

import com.JtoP.Spring.boundedContext.user.input.UserCreateForm;
import com.JtoP.Spring.boundedContext.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signup(@ModelAttribute("userCreateForm") UserCreateForm userCreateForm) {
        return "user/signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("userCreateForm") UserCreateForm userCreateForm, BindingResult bindingResult) {
        String form = "user/signup_form";
        if (bindingResult.hasErrors()){
            return form;
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return form;
        }

        try {
            userService.create(userCreateForm.getUsername(),
                        userCreateForm.getEmail(),
                        userCreateForm.getPassword1());
        } catch (DataIntegrityViolationException e) {
            // DB에 무결성 제약 조건이 위반될 때 발생하는 예외
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return form;
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return form;
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "user/login_form";
    }
}
