package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Member;

import java.util.List;
import java.util.Optional;

/**
 * 스프링데이터JPA를 사용한 장바구니 아이템 관련 DAO입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-30
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-24
 */
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * 회원이 지닌 장바구니 아이템을 모두 가져옵니다.
     *
     * @param member 장바구니를 지닌 Member 엔티티 객체
     * @return 찾아낸 장바구니 아이템 리스트
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-10-30
     */
    List<Cart> findListByMember(Member member);


    /**
     * 회원이 지닌 장바구니 아이템을 모두 가져오며, 상품 엔티티도 페치조인합니다.
     *
     * @param member 장바구니를 지닌 Member 엔티티 객체
     * @return 찾아낸 장바구니 아이템 리스트
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-24
     */
    @Query("select c from Cart c left join fetch c.item where c.member = :member")
    List<Cart> findListByMemberFetchItem(@Param("member") Member member);

    /**
     * id로 장바구니 아이템을 찾아내며, 소유한 회원의 Member 엔티티도 페치조인합니다.
     *
     * @param id 찾을 장바구니 아이템의 id
     * @return 찾아낸 장바구니 아이템을 담은 Optional객체
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-24
     */
    @Query("select c from Cart c left join fetch c.member where c.id = :id")
    Optional<Cart> findByIdFetchMember(@Param("id") Long id);
}
