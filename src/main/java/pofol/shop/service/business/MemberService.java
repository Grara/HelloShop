package pofol.shop.service.business;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.config.DefaultValue;
import pofol.shop.domain.FileEntity;
import pofol.shop.domain.Member;
import pofol.shop.domain.enums.Role;

import pofol.shop.repository.FileRepository;
import pofol.shop.repository.MemberRepository;
import pofol.shop.service.FileService;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * 회원과 관련된 비즈니스로직을 처리하는 Service클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-24
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileRepository fileRepository;
    private final FileService fileService;

    /**
     * Member를 새로 가입시키고 DB에 저장합니다.
     *
     * @param member 가입시킬 Member
     * @return 가입시킨 Member의 id
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-20
     */
    public Long signUp(Member member) {
        if (isDuplicate(member.getUserName())) {
            throw new IllegalStateException("같은 회원명의 Member가 이미 존재합니다.");
        }
        //프로필 기본 이미지 설정
        FileEntity defaultProfile = fileService.findById(DefaultValue.DEFAULT_PROFILE_IMAGE_ID);
        member.setProfileImage(defaultProfile);

        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 같은 회원명의 Member가 DB에 이미 존재하는지 확인합니다.
     *
     * @param username 검사할 회원명
     * @return 있으면 true, 없으면 false
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-30
     */
    public boolean isDuplicate(String username) {
        Optional<Member> findMember = memberRepository.findByUserName(username);
        if (findMember.isPresent()) {
            return true;
        }
        return false;
    }

    /**
     * 테스트용 초기 Member를 생성합니다.
     *
     * @param username 유저명
     * @param password 패스워드
     * @param role     권한
     * @return 생성한 Member의 id
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-23
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-30
     */
    public Long createInitMember(String username, String password, Role role) {
        //패스워드 값을 인코더로 인코딩해서 넣음
        Member member = new Member(username, passwordEncoder.encode(password), role);
        FileEntity defaultProfile = fileService.findById(DefaultValue.DEFAULT_PROFILE_IMAGE_ID);
        member.setProfileImage(defaultProfile);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 입력한 회원명과 일치하는 해당하는 Member를 DB에서 찾습니다.
     *
     * @param username 찾을 Member의 회원명
     * @return 찾은 Member 객체
     * @throws NoSuchElementException 회원명에 해당하는 회원을 찾지 못했을 경우
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-24
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-24
     */
    public Member findByUserName(String username) {
        return memberRepository.findByUserName(username)
                .orElseThrow(() -> new NoSuchElementException("회원명에 해당하는 회원을 찾지 못했습니다. 입력한 회원명 : " + username));
    }

    /**
     * 입력한 이메일과 일치하는 해당하는 Member를 DB에서 찾습니다. <br/>
     *
     * @param email 찾을 Member의 이메일
     * @return 찾은 Member 객체
     * @throws NoSuchElementException 이메일에 해당하는 회원을 찾지 못했을 경우
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-24
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-24
     */
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("이메일에 해당하는 회원을 찾지 못했습니다. 입력한 이메일 : " + email));
    }


}
