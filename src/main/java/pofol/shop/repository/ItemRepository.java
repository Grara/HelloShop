package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pofol.shop.domain.Item;

public interface ItemRepository extends JpaRepository<Item,Long> {
}
