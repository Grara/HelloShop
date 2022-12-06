package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long>, ItemQueryRepository {

    /**
     * 판매자가 같은 Item들을 찾습니다.
     * @param member 판매자
     * @return 찾아낸 Item List
     */
    List<Item> findListByMember(Member member);
}
