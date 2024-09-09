package org.example.jhta_2402_2_final.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.dao.MemberDao;
import org.example.jhta_2402_2_final.model.dto.CustomUserDetails;
import org.example.jhta_2402_2_final.model.dto.member.MemberDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberDao memberDao;
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        MemberDto memberDto = memberDao.selectUserWithRole(userId);  //아이디만 가지고 검증하면 password는 시큐리티가 알아서 처리해준다.
        if(memberDto !=null) {
            return new CustomUserDetails(memberDto);
        }
        throw new UsernameNotFoundException("일치하는 멤버가 없습니다.");
        //return null;
    }
}
