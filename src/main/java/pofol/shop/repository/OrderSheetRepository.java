package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pofol.shop.domain.OrderSheet;

public interface OrderSheetRepository extends JpaRepository<OrderSheet, Long> {
}
