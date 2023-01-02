package pofol.shop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.PortMapperImpl;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import pofol.shop.handler.AccessDeniedHandlerImpl;
import pofol.shop.handler.CustomLoginFailureHandler;
import pofol.shop.handler.OAuth2LoginSuccessHandler;
import pofol.shop.handler.FormLoginSuccessHandler;
import pofol.shop.service.LoginService;

import java.util.Collections;

/**
 * 스프링 시큐리티와 관련된 설정을 하는 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-23
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2023-01-02
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig{
    private final LoginService loginService;
    private final AccessDeniedHandlerImpl accessDeniedHandlerImpl;
    private final CustomLoginFailureHandler customLoginFailureHandler;
    private PortMapperImpl portMapper = new PortMapperImpl();
    private PortResolverImpl portResolver = new PortResolverImpl();
    private LoginUrlAuthenticationEntryPoint entryPoint = new LoginUrlAuthenticationEntryPoint("/login-form");
    @Value("${server.port}")
    private String port; //애플리케이션 포트번호


//    @Bean
//    public WebSecurityCustomizer configure(){
//        //ignoring에 들어간 url은 시큐리티 적용이 안됨, 리소스를 정상적으로 불러들이기위한 코드
//        return web -> web.ignoring().antMatchers("/resources/**");
//    }

    /**
     * 스프링 시큐리티 설정을 커스텀합니다.
     *
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-23
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-29
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String portArgs = port.substring(2); //현재 애플리케이션 포트번호 뒤에 2자리
        portMapper.setPortMappings(Collections.singletonMap(portArgs,"443")); //8080이면 8443이랑 매핑
        portResolver.setPortMapper(portMapper);
        entryPoint.setPortMapper(portMapper);
        entryPoint.setPortResolver(portResolver);
        HttpSessionRequestCache cache = httpSessionRequestCache();
        cache.setPortResolver(portResolver);
        http.setSharedObject(RequestCache.class, cache);

        //시큐리티 적용url, 로그인, 로그아웃 커스텀
        http
                    .exceptionHandling()//예외 핸들링 설정
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(accessDeniedHandlerImpl) //권한부족일 때 예외 핸들링
                .and()
                    .csrf()//csrf토큰 안씀
                        .disable()
                    .headers() //헤더 설정
                        .frameOptions()
                            .sameOrigin() //같은 origin에서는 x frame 허용
                .and()
                    .authorizeRequests() //url별로 접근 가능 권한 설정
                        .antMatchers(HttpMethod.GET,"/admin", "/orders", "/members") //정확히 이 url이면서 get일 경우
                            .hasRole("ADMIN") //어드민만 접근 가능

                        .antMatchers(HttpMethod.POST, "/orders")//정확히 이 url이면서 post일 경우
                            .hasAnyRole("ADMIN", "USER")

                        .antMatchers("/orders/new", "/cart", "/order-sheet",
                                "/items/new", "/mypage") //정확히 이 url일 경우
                            .access("hasAnyRole('ADMIN', 'USER')") //어드민이나 유저일 경우 접근 가능

                        .anyRequest() //나머지는
                            .permitAll() //전부가능
                .and()
                    .formLogin() //시큐리티 기본 폼 로그인 설정
                        .successHandler(formSuccessHandler())
                        .loginPage("/login-form") //로그인 페이지
                        .loginProcessingUrl("/login") //로그인 요청 url, 스프링시큐리티의 기본 url로 설정
                        .failureHandler(customLoginFailureHandler)
                .and()
                    .logout() //로그아웃 설정
                        .logoutSuccessUrl("/") //로그아웃 성공 시 이동 url
                .and()
                    .oauth2Login() //OAuth2 로그인 설정
                        .successHandler(oauth2SuccessHandler())
                        .loginPage("/login-form") //로그인 페이지
                        .userInfoEndpoint().userService(loginService); //세션 정보 등록 서비스

        return http.build();
    }


    @Bean
    public AuthenticationSuccessHandler formSuccessHandler(){
        return new FormLoginSuccessHandler("/");
    }

    @Bean
    public HttpSessionRequestCache httpSessionRequestCache(){
        return new HttpSessionRequestCache();
    }

    @Bean
    public AuthenticationSuccessHandler oauth2SuccessHandler() { return new OAuth2LoginSuccessHandler("/"); }


}