package pofol.shop.repository;

import pofol.shop.domain.Order;
import pofol.shop.dto.OrderSearchCondition;

import java.util.List;

public interface OrderQueryRepository {
    List<Order> search(OrderSearchCondition condition);
}
