package com.JtoP.Spring.boundedContext.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String showMain() {
        return "redirect:/question/list";
    }
}

// 리다이렉트 작동 방식
// 1. 사용자가 웹 브라우저에서 "/" 경로로 요청을 보냄.
// 2. HomeController의 showMain() 메서드가 호출됨.
// 여기서 showMain()은 루트 메서드 root()
// 3. showMain() 메서드는 "redirect:/question/list" 문자열을 반환
// 여기서 서버는 HTTP 302 응답을 반환 후 Location 헤더에 "/question/list" URL을 포함.
// 4. Spring 프레임워크는 이 반환 값을 해석하여 클라이언트에게 "/question/list"로 리다이렉트하라는 HTTP 응답을 보냄.