package org.example.jhta_2402_2_final.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.dao.MemberDao;
import org.example.jhta_2402_2_final.model.dto.MemberDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberDao memberDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public int signin(MemberDto memberDto) {
        MemberDto memberDto02 = MemberDto.builder()
                .userName(memberDto.getUserName())
                .userEmail(memberDto.getUserEmail())
                .userId(memberDto.getUserId())
                .userPassword(bCryptPasswordEncoder.encode(memberDto.getUserPassword()))
                //.role("ROLE_"+signinDto.getRole())
                .role(memberDto.getRole())
                .build();
        return memberDao.signin(memberDto02);
    }
    //생성자 주입이 됨...
}

