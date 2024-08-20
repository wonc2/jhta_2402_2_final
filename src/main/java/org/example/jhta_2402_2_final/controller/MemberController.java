package org.example.jhta_2402_2_final.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.MemberDto;
import org.example.jhta_2402_2_final.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {


    private final MemberService memberService;



    @GetMapping("/login")
    public String login() {
        return "member/login";
    }



    @GetMapping("/signin")
    public String signin() {
        return "member/signin";
    }

    @PostMapping("/signin")
    public String signinProcess(@ModelAttribute MemberDto memberDto) {
        int result = memberService.signin(memberDto);
        return "redirect:/login";
    }



    @GetMapping("/mypage")
    public String mypage() {
        return "member/mypage";
    }

}