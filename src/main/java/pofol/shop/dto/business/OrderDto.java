package pofol.shop.dto.business;

import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.Order;
import pofol.shop.domain.enums.OrderStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private String userName;
    private int orderTotalPrice;
    private LocalDateTime orderDate;
    private OrderStatus status;

    public OrderDto(Order order){
        this.id = order.getId();
        this.userName = order.getMember().getUserName();
        this.orderTotalPrice = order.getOrderTotalPrice();
        this.orderDate = order.getOrderDate();
        this.status = order.getStatus();
    }
}
