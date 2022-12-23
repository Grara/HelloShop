package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Long>, ItemQueryRepository {

    /**
     * 판매자가 같은 Item들을 찾습니다.
     * @param member 판매자
     * @return 찾아낸 Item List
     */
    List<Item> findListByMember(Member member);

    @Query("select i from Item i left join fetch i.comments c left join fetch c.member where i.id = :id")
    Optional<Item> findByIdFetchCommentsWithMember(@Param("id") Long id);
}
