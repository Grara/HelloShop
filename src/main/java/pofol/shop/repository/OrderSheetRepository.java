package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pofol.shop.domain.OrderSheet;

/**
 * 스프링데이터JPA를 사용한 주문시트 관련 DAO입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-10
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-10
 */
public interface OrderSheetRepository extends JpaRepository<OrderSheet, Long> {
}
