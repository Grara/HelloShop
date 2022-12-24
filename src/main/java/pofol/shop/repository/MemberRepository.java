package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pofol.shop.domain.Member;

import java.util.List;
import java.util.Optional;

/**
 * 스프링데이터JPA를 사용한 회원 관련 DAO입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-08
 */
public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryRepository {

    /**
     * 회원명으로 Member를 찾고 Optional로 반환합니다.
     *
     * @param username 찾을 회원명
     * @return 찾아낸 Member를 담은 Optional 객체
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-08
     */
    Optional<Member> findByUserName(String username);

    /**
     * 이메일로 Member를 찾고 Optional로 반환합니다.
     *
     * @param email 찾을 회원의 이메일주소
     * @return 찾아낸 Member를 담은 Optional 객체
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-08
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-08
     */
    Optional<Member> findByEmail(String email);
}
