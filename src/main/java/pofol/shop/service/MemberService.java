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
import pofol.shop.domain.FileEntity;
import pofol.shop.domain.Member;
import pofol.shop.domain.enums.Role;
import pofol.shop.repository.FileRepository;
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
    private final FileRepository fileRepository;

    /**
     * Member를 새로 가입시키고 DB에 저장합니다.
     * @param member 가입시킬 Member
     * @return 가입시킨 Member의 id
     */
    public Long signUp(Member member) {
        if(isDuplicate(member.getUserName())) {
            throw new IllegalStateException("같은 회원명의 Member가 이미 존재합니다.");
        }
        //프로필 기본 이미지 설정
        FileEntity defaultProfile = fileRepository.findById(DefaultValue.DEFAULT_PROFILE_IMAGE_ID).orElseThrow();
        member.setProfileImage(defaultProfile);

        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 같은 회원명의 Member가 DB에 이미 존재하는지 확인합니다.
     * @param username 검사할 회원명
     * @return 있으면 true, 없으면 false
     */
    public boolean isDuplicate(String username){
        Optional<Member> findMember = memberRepository.findByUserName(username);
        if (findMember.isPresent()) {
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
        FileEntity defaultProfile = fileRepository.findById(DefaultValue.DEFAULT_PROFILE_IMAGE_ID).orElseThrow();
        member.setProfileImage(defaultProfile);
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
