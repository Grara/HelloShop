package pofol.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class EtcConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        //패스워드 인코더 등록
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
