package pofol.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class EtcConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        //패스워드 인코더 등록, 원래 SecurityConfig에서 생성했으나 계속 오류가 발생해서 옮김
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
