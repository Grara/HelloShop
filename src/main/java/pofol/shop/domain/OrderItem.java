package pofol.shop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class OrderItem {
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

    private int itemsTotalPrice;

    public OrderItem(CartItem cartItem){
        this.item = cartItem.getItem();
        this.count = cartItem.getCount();
        this.itemsTotalPrice = cartItem.getItemsTotalPrice();
    }

    public OrderItem(Item item, int count) {
        this.item = item;
        this.count = count;
        this.itemsTotalPrice = item.getPrice() * count;
    }
}
