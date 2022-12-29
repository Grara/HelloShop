package pofol.shop.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pofol.shop.domain.Member;
import pofol.shop.dto.security.UserAdapter;
import pofol.shop.repository.MemberRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * OAuth2 로그인 성공 시 핸들링을 설정하는 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-29
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-29
 */
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    private MemberRepository memberRepository;

    public OAuth2LoginSuccessHandler(String defaultTargetUrl){
        super.setDefaultTargetUrl(defaultTargetUrl);
    }
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        HttpSession session = request.getSession();
        UserAdapter principal = (UserAdapter) authentication.getPrincipal();

        Optional<Member> member = memberRepository.findByEmail(principal.getAttribute("email"));

        if (member.isPresent()) {
            if (session != null) {
                String redirectUrl = (String) session.getAttribute("redirectUrl");
                if (redirectUrl != null) {
                    getRedirectStrategy().sendRedirect(request, response, redirectUrl);
                }
                else {
                    super.onAuthenticationSuccess(request, response, authentication);
                }
            }
            else super.onAuthenticationSuccess(request, response, authentication);
        }
        else {
            getRedirectStrategy().sendRedirect(request, response, "/members/new-oauth2");
        }
    }
}
