package pofol.shop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.enums.OrderStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private int itemKind;
    private int orderTotalPrice;
    private LocalDateTime orderDate;

    public Order(Member member, OrderStatus status) {
        this.member = member;
        this.status = status;
    }

    public void addOrderItem(OrderItem orderItem){
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, Address address , List<OrderItem> orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(new Delivery(address));
        order.setStatus(OrderStatus.ORDER);
        int totalPrice = 0;
        int kind = 0;
        for(OrderItem item : orderItems){
            totalPrice += item.getItemsTotalPrice();
            kind++;
            order.addOrderItem(item);
        }
        order.setItemKind(kind);
        order.setOrderTotalPrice(totalPrice);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
}
