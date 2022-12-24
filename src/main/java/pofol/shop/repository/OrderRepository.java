package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pofol.shop.domain.Order;

/**
 * 스프링데이터JPA를 사용한 주문 관련 DAO입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-21
 */
public interface OrderRepository extends JpaRepository<Order, Long>, OrderQueryRepository {


}
