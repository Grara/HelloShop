package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long> {

    List<Item> findListByMember(Member member);
}
