package org.example.jhta_2402_2_final.service.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            response.sendRedirect("index/index");
        }else if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PRODUCT_MANAGER"))){
            response.sendRedirect("/product/company");
        }else if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_LOGISTICS_MANAGER"))){
            response.sendRedirect("/");
        }else if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SALES_MANAGER"))){
            response.sendRedirect("/");
        }
    }
}
