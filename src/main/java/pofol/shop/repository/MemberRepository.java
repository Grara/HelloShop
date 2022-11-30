package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pofol.shop.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryRepository{

    /**
     * 회원명으로 Member를 찾고 Optional로 반환합니다.
     * @param username 찾을 회원명
     * @return 찾아낸 Member (Optional)
     */
    Optional<Member> findByUserName(String username);
}
