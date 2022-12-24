package pofol.shop.domain;

import lombok.*;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.enums.OrderStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 주문에 대한 엔티티 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-24
 */
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

    @OneToMany(mappedBy = "order") //단방향 일대다
    @Setter(AccessLevel.NONE) //연관관계의 주인이 아님
    private List<OrderItem> orderItems;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private int orderTotalPrice;
    private LocalDateTime orderDate;
    //----------필드 끝 / 생성자 시작----------//

    //----------생성자 끝 / 메소드 시작----------//

    /**
     * 주문에 주문 아이템을 추가합니다.
     *
     * @param orderItem 추가할 주문 아이템
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-10-21
     */
    public void addOrderItem(OrderItem orderItem){
        orderItem.setOrder(this);
    }

    /**
     * 새로운 주문 객체를 생성합니다.
     *
     * @param member 주문자
     * @param delivery 배송정보
     * @param orderItems 주문아이템 리스트
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-30
     */
    public static Order createOrder(Member member, Delivery delivery , List<OrderItem> orderItems){

        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        order.setStatus(OrderStatus.ORDER);

        int totalPrice = 0;

        for(OrderItem item : orderItems){
            totalPrice += item.getTotalPrice();
            order.addOrderItem(item);
        }

        order.setOrderTotalPrice(totalPrice);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    /**
     * 주문을 취소합니다.
     *
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-24
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-24
     */
    public void cancel(){
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem item : orderItems){
            item.cancel();
        }
    }
}
