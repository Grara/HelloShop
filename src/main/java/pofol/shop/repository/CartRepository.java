package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Member;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Member가 같은 Cart들을 찾습니다.
     * @param member Cart들을 지닌 Member
     * @return 찾은 Cart List
     */
    List<Cart> findListByMember(Member member);


    /**
     * Member가 같은 Cart들을 찾고 각 Cart의 Item을 페치조인합니다.
     * @param member Cart들을 지닌 Member
     * @return 찾은 Cart List
     */
    @Query("select c from Cart c left join fetch c.item where c.member = :member")
    List<Cart> findListByMemberFetchItem(@Param("member") Member member);


    @Query("select c from Cart c left join fetch c.member where c.id = :id")
    Optional<Cart> findByIdFetchMember(@Param("id") Long id);
}
