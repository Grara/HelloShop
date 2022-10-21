package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pofol.shop.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
