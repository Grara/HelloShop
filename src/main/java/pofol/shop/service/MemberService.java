package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.Member;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.embedded.PersonalInfo;
import pofol.shop.repository.MemberRepository;
import pofol.shop.repository.MemberRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Long join(Member member){
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

    public void delete(Member member){
        memberRepository.delete(member);
    }

    public Long initMember(String username, String password){
        Address address = new Address("서울", "신림", 111);
        PersonalInfo personal = new PersonalInfo("노민준", 28);
        //패스워드 값을 인코더로 인코딩해서 넣음
        Member member = new Member(username, passwordEncoder.encode(password), address, personal);
        memberRepository.save(member);
        return member.getId();
    }

    @Override
    //UserDetailsService의 메소드 구현
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> byUserName = memberRepository.findByUserName(username);
        //유저이름을 찾은 뒤 없으면 에러를 던짐
        Member findMember = byUserName.orElseThrow(()->new UsernameNotFoundException(username));
        return new User(findMember.getUserName(), findMember.getPassword(), authorities());
    }

    private Collection<? extends GrantedAuthority> authorities() {
        //인가 정보가 필요함
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
