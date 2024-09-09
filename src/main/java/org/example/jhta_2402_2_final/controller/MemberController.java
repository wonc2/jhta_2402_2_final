package org.example.jhta_2402_2_final.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.member.MemberDto;
import org.example.jhta_2402_2_final.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {


    private final MemberService memberService;


    @ResponseBody
    @GetMapping("/logTest")
    public void logTest() {
        // 인증된 유저 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // UserDetails 타입으로 캐스팅하여 유저의 상세 정보를 가져올 수 있음
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            // 유저의 이름 출력
            System.out.println("Authenticated user: " + username +"///");
            System.out.println(">>>>>>>>>"+userDetails);
        } else {
            // 인증된 사용자가 없을 경우 처리
            System.out.println("No authenticated user found.");
        }
    }


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
        int result2 = memberService.insertRole(memberDto.getUserId());
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