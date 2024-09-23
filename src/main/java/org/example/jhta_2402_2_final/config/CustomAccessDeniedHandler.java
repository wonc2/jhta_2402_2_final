package org.example.jhta_2402_2_final.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.enums.Role;
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
        Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());

        // 권한에 따라 리다이렉트 경로 설정
        if (roles.contains(Role.ROLE_ADMIN)) {
            response.sendRedirect("/");
        } else if (roles.contains(Role.ROLE_PRODUCT_MANAGER)) {
            response.sendRedirect("/product/company");
        } else if (roles.contains(Role.ROLE_LOGISTICS_MANAGER)) {
            response.sendRedirect("/wareHouse/selectAll");
        } else if (roles.contains(Role.ROLE_SALES_MANAGER)) {
            response.sendRedirect("/sales/user");
        } else {
            // 권한이 없는 경우 기본적인 접근 권한 부족 페이지가 아니라 해당 권한 페이지로 이동
            redirectToNearestRolePage(response, roles);
        }

    }
    private void redirectToNearestRolePage(HttpServletResponse response, Set<String> roles) throws IOException {
        // 관리자 권한이 없으면, 다른 권한 중 첫 번째로 이동
        if (roles.contains(Role.ROLE_ADMIN.name())) {
            response.sendRedirect("/");
        } else if (roles.contains(Role.ROLE_PRODUCT_MANAGER.name())) {
            response.sendRedirect("/product/company");
        } else if (roles.contains(Role.ROLE_LOGISTICS_MANAGER.name())) {
            response.sendRedirect("/wareHouse/selectAll");
        } else if (roles.contains(Role.ROLE_SALES_MANAGER.name())) {
            response.sendRedirect("/sales/user");
        } else {
            response.sendRedirect("/logout");
        }
    }
}