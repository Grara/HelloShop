package pofol.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pofol.shop.domain.Order;
import pofol.shop.dto.business.OrderDto;
import pofol.shop.dto.business.OrderSearchCondition;

import java.util.List;

/**
 * OrderQueryRepositoryImpl을 만들기 위한 인터페이스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-30
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-01
 */
public interface OrderQueryRepository {

    List<Order> search(OrderSearchCondition condition);

    Page<OrderDto> searchWithPage(OrderSearchCondition condition, Pageable pageable);
}
