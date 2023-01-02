package pofol.shop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@EnableJdbcHttpSession
@Configuration
public class WebMvcConfigure implements WebMvcConfigurer {
}
