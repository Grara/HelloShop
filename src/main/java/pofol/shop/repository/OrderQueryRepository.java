package pofol.shop.repository;

import pofol.shop.domain.Order;
import pofol.shop.dto.OrderSearchCondition;

import java.util.List;

public interface OrderQueryRepository {

    /**
     * 주어진 검색조건으로 Order를 찾습니다.
     * @param condition 검색조건
     * @return 찾아낸 Order List
     */
    List<Order> search(OrderSearchCondition condition);
}
