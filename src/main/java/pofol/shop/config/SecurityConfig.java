package pofol.shop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pofol.shop.service.CustomOAuth2MemberService;


@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2MemberService customOAuth2MemberService;

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
                        .antMatchers("/admin", "/members", "/orders").hasRole("ADMIN")
                        .antMatchers("/orders/new", "/cart/new", "/orderSheet",
                                "/items/new", "/mypage").hasAnyRole("ADMIN", "USER")
                        .anyRequest().permitAll()
                .and()
                    .formLogin()
                .and()
                    .logout().logoutSuccessUrl("/")
                .and()
                    .oauth2Login().userInfoEndpoint().userService(customOAuth2MemberService);

        return http.build();
    }

}