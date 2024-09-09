package org.example.jhta_2402_2_final.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/*
* [code-review] 시큐리티 설정 힘드셨을 텐데 잘하셨습니다.
* 여기서 더 나아가서 현재 어떻게 세션이 관리가 되고 있는지
* 현재 방법은 어떤 문제가 있기에 데이터 베이스를 사용하는지
* 데이터 베이스로 세션을 변경시켜 보는 것을 추천드립니다.
* */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationFailureHandler customFailureHandler;

    public SecurityConfig(AuthenticationFailureHandler customFailureHandler) {
        this.customFailureHandler = customFailureHandler;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers(
                "/css/**", "/js/**", "/images/**", "/h2-console/**", "/error/**", "/api/**")
        );
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
        httpSecurity.authorizeHttpRequests((auth)->auth
                .requestMatchers("/","/member/**","/test","/adminMain/**","/product/**","/sales/**","/distribution/**","/wareHouse/**","/distributionOrder/**")
                .permitAll()
                .requestMatchers("/product/role").hasAuthority("ROLE_PRODUCT_MANAGER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasAnyRole("ADMIN","USER")
                .requestMatchers("/manager/**").hasAnyRole("ADMIN","MANAGER")
                .anyRequest()
                .authenticated()
        );
        httpSecurity.formLogin((auth)->auth
                .loginPage("/member/login")
                .loginProcessingUrl("/member/login")
                .usernameParameter("userId")
                .passwordParameter("userPassword")
                .defaultSuccessUrl("/",true) //true를 쓰지 않으면 이전 페이지로 간다.
                .failureHandler(customFailureHandler)  //로그인 실패시 어떻게 할건지를 니가 만들어서 처리해라
                .permitAll()
        );
        httpSecurity.logout((auth)->auth
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
        );
        return httpSecurity.build();
    }
}
