package pofol.shop.dto.business;

import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.Order;
import pofol.shop.domain.enums.OrderStatus;

import java.time.LocalDateTime;

/**
 * 주문의 DTO클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-01
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-01
 */
@Data
@NoArgsConstructor
public class OrderDto {
    private Long id; //주문 id
    private String userName; //주문자
    private int orderTotalPrice; //주문 총 가격
    private LocalDateTime orderDate; //주문 날짜
    private OrderStatus status; //주문 상태

    public OrderDto(Order order){
        this.id = order.getId();
        this.userName = order.getMember().getUserName();
        this.orderTotalPrice = order.getOrderTotalPrice();
        this.orderDate = order.getOrderDate();
        this.status = order.getStatus();
    }
}
