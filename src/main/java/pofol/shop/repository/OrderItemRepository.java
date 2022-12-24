package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Member;
import pofol.shop.domain.Order;
import pofol.shop.domain.OrderItem;

import java.util.List;

/**
 * 스프링데이터JPA를 사용한 주문아이템 관련 DAO입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-30
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {


    List<OrderItem> findListByOrder(Order order);

    /**
     * 특정 Order와 연관된 OrderItem들을 찾고 각 OrderItem들의 Item을 페치 조인 합니다.
     *
     * @param order OrderItem들이 연관된 Order
     * @return 찾아낸 OrderItem List
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-30
     */
    @Query("select oi from OrderItem oi left join fetch oi.item where oi.order = :order")
    List<OrderItem> findListByOrderFetchItem(@Param("order") Order order);
}
