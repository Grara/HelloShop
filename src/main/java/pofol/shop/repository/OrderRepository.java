package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pofol.shop.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderQueryRepository {
}
