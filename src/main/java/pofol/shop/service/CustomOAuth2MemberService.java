package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import pofol.shop.domain.Member;
import pofol.shop.dto.SessionUser;
import pofol.shop.repository.MemberRepository;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2MemberService implements OAuth2UserService {
    private final MemberRepository memberRepository;
    private final HttpSession session;
    private final PasswordEncoder encoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttrName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attr = OAuthAttributes.of(registrationId, userNameAttrName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attr, encoder);
        session.setAttribute("user", new SessionUser(member));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().toString())),
                attr.getAttributes(),
                attr.getNameAttributeKey());
    }

    private Member saveOrUpdate(OAuthAttributes attr, PasswordEncoder encoder){
        Member member = memberRepository.findByEmail(attr.getEmail())
                .map(entity -> entity.update(attr.getName(), attr.getEmail())).orElse(attr.toEntity(encoder));

        return memberRepository.save(member);
    }
}
