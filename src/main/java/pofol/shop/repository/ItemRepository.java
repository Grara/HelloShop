package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;

import java.util.List;
import java.util.Optional;

/**
 * 스프링데이터JPA를 사용한 상품 관련 DAO입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-24
 */
public interface ItemRepository extends JpaRepository<Item,Long>, ItemQueryRepository {

    /**
     * 판매자가 같은 상품들을 DB에서 찾습니다.
     * @param member 판매자
     * @return 찾아낸 상품 List
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-30
     */
    List<Item> findListByMember(Member member);

    /**
     * id로 상품을 DB에서 찾으며, 상품의 후기와 후기 작성회원의 엔티티객체도 페치조인합니다.
     * @param id 찾을 상품의 id
     * @return 찾아낸 상품을 담은 Optional객체
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-24
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-24
     */
    @Query("select i from Item i left join fetch i.comments c left join fetch c.member where i.id = :id")
    Optional<Item> findByIdFetchCommentsWithMember(@Param("id") Long id);
}
