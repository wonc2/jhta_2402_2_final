package org.example.jhta_2402_2_final.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.service.login.CustomLoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // 현재 인증된 사용자의 권한 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());

            // 권한에 따라 리다이렉트 경로 설정
            if (roles.contains("ROLE_ADMIN")) {
                response.sendRedirect("/");
            } else if (roles.contains("ROLE_PRODUCT_MANAGER")) {
                response.sendRedirect("/product/company");
            } else if (roles.contains("ROLE_LOGISTICS_MANAGER")) {
                response.sendRedirect("/wareHouse/selectAll");
            } else if (roles.contains("ROLE_SALES_MANAGER")) {
                response.sendRedirect("/sales/user");
            } else {
                response.sendRedirect("/accessDenied"); // 권한이 맞지 않을 경우 공통 에러 페이지
            }
        } else {
            response.sendRedirect("/accessDenied"); // 인증되지 않은 경우 공통 에러 페이지
        }
    }
}