package pofol.shop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import pofol.shop.service.LoginService;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig{

    private final LoginService loginService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public WebSecurityCustomizer configure(){
        //ignoring에 들어간 url은 시큐리티 적용이 안됨, 리소스를 정상적으로 불러들이기위한 코드
        return web -> web.ignoring().antMatchers("/resources/**");
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //시큐리티 적용url, 로그인, 로그아웃 커스텀
        http
                .csrf().disable()
                    .authorizeRequests()
                        .antMatchers("/admin", "/members", "/orders")
                            .hasRole("ADMIN")
                        .antMatchers("/orders/new", "/cart/new", "/orderSheet",
                                "/items/new", "/mypage")
                            .hasAnyRole("ADMIN", "USER")
                        .antMatchers("/login")
                            .denyAll()
                        .anyRequest().permitAll()
                .and()
                    .formLogin()
                        .loginPage("/login-form")
                        .loginProcessingUrl("/login")
                        .failureUrl("/login-form?error=true")
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                        .loginPage("/login-form")
                        .defaultSuccessUrl("/members/new-oauth2")
                        .userInfoEndpoint().userService(loginService);


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

}