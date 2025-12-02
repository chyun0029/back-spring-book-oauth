package me.book_ch8.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    @GetMapping("/login")
    public String login(){
//        return "login";
        return "oauthLogin"; // 10장
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }
}
