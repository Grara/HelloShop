package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import pofol.shop.domain.Member;
import pofol.shop.dto.business.MemberDto;
import pofol.shop.dto.security.OAuthAttributes;
import pofol.shop.dto.security.UserAdapter;
import pofol.shop.repository.MemberRepository;
import pofol.shop.service.business.MemberService;

/**
 * 로그인 시 유저의 로그인 세션 정보를 등록해주는 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-08
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-12
 */
@Service
@RequiredArgsConstructor
public class LoginService implements OAuth2UserService, UserDetailsService {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PasswordEncoder encoder;

    //OAuth2 로그인 시 유저 정보 등록
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttrName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();


        OAuthAttributes attr = OAuthAttributes.of(registrationId, userNameAttrName, oAuth2User.getAttributes());


        Member member = memberRepository.findByEmail(attr.getEmail()).orElse(attr.toEntity(encoder));
        MemberDto dto = new MemberDto(member);

        return new UserAdapter(dto, attr.getAttributes());
    }

    @Override
    //UserDetailsService의 메소드 구현
    //로그인 시 자동으로 시큐리티에서 관리할 유저정보를 생성
    public UserDetails loadUserByUsername(String username) {
        Member member = memberService.findByUserName(username);
        MemberDto dto = new MemberDto(member); //Proxy 관련 문제로 dto를 생성
        return new UserAdapter(dto);
    }
}
