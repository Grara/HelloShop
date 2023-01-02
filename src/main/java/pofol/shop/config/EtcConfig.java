package pofol.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * 기타 설정을 하는 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-08
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-08
 */
@Configuration
public class EtcConfig {


    /**
     * 패스워드 인코더를 빈으로 등록합니다. <br/>
     * SecurityConfig에서 등록했으나, 순환참조 문제로 여기서 등록
     *
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-08
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-08
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        //패스워드 인코더 등록, 원래 SecurityConfig에서 생성했으나 계속 오류가 발생해서 옮김
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
