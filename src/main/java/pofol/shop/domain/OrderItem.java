package pofol.shop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.formAndDto.OrderItemDto;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class OrderItem extends BaseEntity{
    //----------필드 시작----------//
    @Id @GeneratedValue
    @Column(name = "orderitem_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int count;

    private int totalPrice;
    //----------필드 끝----------//

    //----------생성자 시작----------//
    public OrderItem(Cart cart){
        this.item = cart.getItem();
        this.count = cart.getCount();
        this.totalPrice = cart.getTotalPrice();
    }

    public OrderItem(Item item, int count) {
        this.item = item;
        this.count = count;
        this.totalPrice = item.getPrice() * count;
    }
    //----------생성자 끝----------//


}
