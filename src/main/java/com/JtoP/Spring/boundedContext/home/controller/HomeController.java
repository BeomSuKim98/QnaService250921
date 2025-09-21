package com.JtoP.Spring.boundedContext.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/home")
public class HomeController {
    @GetMapping("/main")
    @ResponseBody
    public String showMain() {
        return "This is the main page.";
    }
}