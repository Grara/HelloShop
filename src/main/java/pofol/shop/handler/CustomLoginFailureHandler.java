package pofol.shop.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * 폼 로그인 실패 시 핸들링을 설정하는 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-28
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-28
 */
@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    //로그인 실패 시 리다이렉션
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String redirectUrl = (String)request.getSession().getAttribute("redirectUrl");
        if (redirectUrl != null) {
            setDefaultFailureUrl("/login-form?error=true&redirect-url=" + redirectUrl);
        }
        else{
            setDefaultFailureUrl("/login-form?error=true");
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}
