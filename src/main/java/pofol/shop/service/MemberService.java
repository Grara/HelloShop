package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.Member;
import pofol.shop.domain.enums.Role;
import pofol.shop.repository.MemberRepository;

import javax.persistence.EntityNotFoundException;
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

    /**
     * 전체 Member목록을 반환합니다.
     *
     * @return 전체 Member목록
     */
    public List<Member> findList() {
        return memberRepository.findAll();
    }

    /**
     * id를 통해 Member를 찾고 반환합니다.
     *
     * @param id 찾을 Member의 id
     * @return 찾아낸 Member
     */
    public Member findOne(Long id) throws Exception{
        return memberRepository.findById(id).orElseThrow(()->new EntityNotFoundException());
    }

    /**
     * 유저명을 통해 Member를 찾고 반환합니다.
     *
     * @param name 찾을 Member의 유저명
     * @return 찾아낸 Member
     */
    public Member findOneByName(String name) throws Exception {
        return memberRepository.findByUserName(name).orElseThrow(()->new EntityNotFoundException());
    }

    /**
     * Member를 새로 가입시킵니다.
     *
     * @param member 가입시킬 Member
     * @return 가입시킨 Member의 id
     */
    public Long signUp(Member member) {
        duplicateCheck(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 인자로 들어온 Member의 유저명과 같은 Member가 있는지 중복체크합니다.
     *
     * @param member 중복체크할 Member
     */
    public void duplicateCheck(Member member) {
        List<Member> findMembers = memberRepository.findListByUserName(member.getUserName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원임미다");
        }
    }

    /**
     * Member를 회원탈퇴시킵니다.
     *
     * @param member 탈퇴시킬 Member
     */
    public void signOut(Member member) {
        memberRepository.delete(member);
    }

    /**
     * 테스트용 초기 Member를 생성합니다.
     *
     * @param username 유저명
     * @param password 패스워드
     * @param role     권한
     * @return 생성한 Member의 id
     */
    public Long createInitMember(String username, String password, Role role) {
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
        Member findMember = byUserName.orElseThrow(() -> new UsernameNotFoundException(username));
        return new User(findMember.getUserName(), findMember.getPassword(), createAuthorities(findMember.getRole().toString()));
    }

    //인가정보 생성
    private Collection<? extends GrantedAuthority> createAuthorities(String role) {
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }
}
