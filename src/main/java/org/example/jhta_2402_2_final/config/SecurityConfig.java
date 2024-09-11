package org.example.jhta_2402_2_final.config;

import org.example.jhta_2402_2_final.service.login.CustomLoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
    public CustomLoginSuccessHandler customLoginSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers(
                "/css/**", "/js/**", "/images/**", "/h2-console/**", "/error/**")
        );
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
        httpSecurity.authorizeHttpRequests((auth)->auth
                .requestMatchers("/","/member/**","/test","/adminMain/**","/sales/**","/distribution/**","/wareHouse/**","/distributionOrder/**" ,"/api/**")
                .permitAll()
                .requestMatchers("/product/role").hasAuthority("ROLE_PRODUCT_MANAGER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasAnyRole("ADMIN","USER")
                .requestMatchers("/manager/**").hasAnyRole("ADMIN","MANAGER")
                .requestMatchers("/distribution/**").hasAnyRole("ADMIN","MANAGER")
//                .requestMatchers("/product/company/**").hasAnyRole("ADMIN","PRODUCT_MANAGER")
//                .requestMatchers("/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
        );
        httpSecurity.formLogin((auth)->auth
                .loginPage("/member/login")
                .loginProcessingUrl("/member/login")
                .usernameParameter("userId")
                .passwordParameter("userPassword")
                .defaultSuccessUrl("/",true) //true를 쓰지 않으면 이전 페이지로 간다.
//                .successHandler(customLoginSuccessHandler())
                .failureHandler(customFailureHandler)  //로그인 실패시 어떻게 할건지를 니가 만들어서 처리해라
                .permitAll()
        );
        httpSecurity.logout((auth)->auth
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
        );
        httpSecurity.csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        );

        return httpSecurity.build();
    }
}
