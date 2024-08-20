package org.example.jhta_2402_2_final.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.MemberDto;
import org.example.jhta_2402_2_final.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        int result = memberService.insertUser(memberDto);
        return "redirect:/login";
    }

    @GetMapping("/checkUserId")
    public ResponseEntity<Boolean> checkUserId(@RequestParam String userId) {
        try {
            boolean isAvailable = memberService.isUserIdAvailable(userId);
            return ResponseEntity.ok(!isAvailable);
        } catch (Exception e) {
            // 오류 로그
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "member/mypage";
    }

}