package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.Member;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.embedded.PersonalInfo;
import pofol.shop.domain.enums.Role;
import pofol.shop.repository.MemberRepository;
import pofol.shop.repository.MemberRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Member> findList(){
        return memberRepository.findAll();
    }
    public Member findOne(Long id){
        return memberRepository.findById(id).get();
    }
    public Member findOneByName(String name){
        return memberRepository.findByUserName(name).get();
    }


    public Long signUp(Member member){
        duplicateCheck(member);
        memberRepository.save(member);
        return member.getId();
    }

    public void duplicateCheck(Member member){
        List<Member> findMembers = memberRepository.findListByUserName(member.getUserName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원임미다");
        }
    }

    public void signOut(Member member){
        memberRepository.delete(member);
    }

    public Long createMember(String username, String password, Role role){
        //패스워드 값을 인코더로 인코딩해서 넣음
        Member member = new Member(username, passwordEncoder.encode(password), role);
        memberRepository.save(member);
        return member.getId();
    }

    @Override
    //UserDetailsService의 메소드 구현
    //로그인 시 자동으로 시큐리티에서 관리할 유저정보를 생성
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> byUserName = memberRepository.findByUserName(username);
        //유저이름을 찾은 뒤 없으면 에러를 던짐
        Member findMember = byUserName.orElseThrow(()->new UsernameNotFoundException(username));
        return new User(findMember.getUserName(), findMember.getPassword(), createAuthorities(findMember.getRole().toString()));
    }

    private Collection<? extends GrantedAuthority> createAuthorities(String role) {
        //인가 정보가 필요함
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }
}
