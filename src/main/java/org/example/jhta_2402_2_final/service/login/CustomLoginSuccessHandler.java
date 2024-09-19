package org.example.jhta_2402_2_final.service.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        HttpSession session = request.getSession();
        if(roles.contains("ROLE_ADMIN")){
            response.sendRedirect("/");
        }else if(roles.contains("ROLE_PRODUCT_MANAGER")){
            response.sendRedirect("/product/company");
        }else if(roles.contains("ROLE_LOGISTICS_MANAGER")){
            response.sendRedirect("/wareHouse/selectAll");
        }else if(roles.contains("ROLE_SALES_MANAGER")){
            response.sendRedirect("/sales");
        }
    }
}
