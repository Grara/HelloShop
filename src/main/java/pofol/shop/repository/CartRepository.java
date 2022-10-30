package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Member;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByMember(Member member);

    @Query("select c from Cart c left join fetch c.item where c.member = :member")
    List<Cart> findByMemberFetchItem(@Param("member") Member member);
}
