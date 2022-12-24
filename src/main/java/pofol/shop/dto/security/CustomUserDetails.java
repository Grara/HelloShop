package pofol.shop.dto.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import pofol.shop.dto.business.MemberDto;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * 폼 로그인, OAuth2 로그인 시 등록되는 유저 세션 정보입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-12
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-12
 */
public class CustomUserDetails implements UserDetails, OAuth2User {

    private MemberDto member;
    private Map<String, Object> attributes;

    //폼 로그인 시
    public CustomUserDetails(MemberDto member) {
        this.member = member;
    }

    //OAuth로그인 시
    public CustomUserDetails(MemberDto member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(member.getRole().toString()));
    }

    public void setMember(MemberDto member) {
        this.member = member;
    }

    @Override
    public String getName() {
        return member.getUserName();
    }

    public void setName(String name) { this.member.setUserName(name); }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
