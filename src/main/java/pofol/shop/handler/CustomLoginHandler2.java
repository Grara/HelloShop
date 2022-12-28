package pofol.shop.handler;

import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLoginHandler2 extends SimpleUrlAuthenticationSuccessHandler {

    public CustomLoginHandler2(){
        super();
        setUseReferer(true);
    }
}
