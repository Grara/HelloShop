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

    @ManyToOne(fetch = FetchType.LAZY) //단방향 다대일
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) //단방향 일대일
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private int itemKind;
    private int orderTotalPrice;
    private LocalDateTime orderDate;
    //----------필드 끝 / 생성자 시작----------//
    public Order(Member member, OrderStatus status) {
        this.member = member;
        this.status = status;
    }
    //----------생성자 끝 / 메소드 시작----------//
    public void addOrderItem(OrderItem orderItem){
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, Delivery delivery , List<OrderItem> orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        order.setStatus(OrderStatus.ORDER);
        int totalPrice = 0;
        int kind = 0;
        for(OrderItem item : orderItems){
            totalPrice += item.getTotalPrice();
            kind++;
            order.addOrderItem(item);
        }
        order.setItemKind(kind);
        order.setOrderTotalPrice(totalPrice);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
}
