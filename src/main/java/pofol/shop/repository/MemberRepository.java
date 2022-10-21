package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pofol.shop.domain.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public List<Member> findMemberByUserName(String username);
}
