package pofol.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer configure(){
        //ignoring에 들어간 url은 시큐리티 적용이 안됨, 리소스를 정상적으로 불러들이기위한 코드
        return web -> web.ignoring().antMatchers("/resources/**");
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //시큐리티 적용url, 로그인, 로그아웃 커스텀
        return http.authorizeRequests()
                .antMatchers("/members").hasRole("ADMIN")
                .antMatchers("/orders/new", "/cart/new").hasAnyRole("ADMIN", "USER")
                .anyRequest().permitAll()
                .and()
                .formLogin().and()
                .logout().logoutSuccessUrl("/").and()
                .csrf().disable()
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        //패스워드 인코더 등록
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}