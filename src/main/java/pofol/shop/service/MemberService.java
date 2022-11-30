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
import pofol.shop.config.DefaultValue;
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
    private final FileService fileService;


    /**
     * Member를 새로 가입시키고 DB에 저장합니다.
     * @param member 가입시킬 Member
     * @return 가입시킨 Member의 id
     */
    public Long signUp(Member member) {
        if(isDuplicate(member.getUserName())) {
            throw new IllegalStateException("같은 회원명의 Member가 이미 존재합니다.");
        }
        memberRepository.save(member);
        //프로필 기본 이미지 설정
        member.setProfileImage(fileService.findOne(DefaultValue.DEFAULT_PROFILE_IMAGE_ID));
        return member.getId();
    }
    /**
     * 모든 Member를 DB에서 찾습니다.
     * @return 전체 Member의 List
     */
    public List<Member> findList() {
        return memberRepository.findAll();
    }

    /**
     * id를 통해 Member를 DB에서 찾습니다.
     * @param id 찾을 Member의 id
     * @return 찾아낸 Member
     */
    public Member findOne(Long id){
        return memberRepository.findById(id).orElseThrow(()->new EntityNotFoundException("member not found"));
    }

    /**
     * 유저명을 통해 DB에서 Member를 찾습니다.
     * @param name 찾을 Member의 유저명
     * @return 찾아낸 Member
     */
    public Member findOneByName(String name){
        return memberRepository.findByUserName(name).orElseThrow(()->new EntityNotFoundException("member not found"));
    }

    /**
     * Member를 DB에 저장합니다.
     * @param member 저장할 Member
     * @return
     */
    public Long save(Member member){
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * Member를 DB에서 삭제합니다.
     * @param member 삭제할 Member
     */
    public void signOut(Member member){
        memberRepository.delete(member);
    }

    /**
     * 같은 회원명의 Member가 DB에 이미 존재하는지 확인합니다.
     * @param username 검사할 회원명
     * @return 있으면 true, 없으면 false
     */
    public boolean isDuplicate(String username){
        List<Member> findMembers = memberRepository.findListByUserName(username);
        if (!findMembers.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 테스트용 초기 Member를 생성합니다.
     * @param username 유저명
     * @param password 패스워드
     * @param role     권한
     * @return 생성한 Member의 id
     */
    public Long createInitMember(String username, String password, Role role){
        //패스워드 값을 인코더로 인코딩해서 넣음
        Member member = new Member(username, passwordEncoder.encode(password), role);
        member.setProfileImage(fileService.findOne(DefaultValue.DEFAULT_PROFILE_IMAGE_ID));
        memberRepository.save(member);
        return member.getId();
    }

    @Override
    //UserDetailsService의 메소드 구현
    //로그인 시 자동으로 시큐리티에서 관리할 유저정보를 생성
    public UserDetails loadUserByUsername(String username) {
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
