package pofol.shop.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Cart extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "cartitem_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    private int totalPrice;

    public Cart(Member member, Item item, int count) {
        this.member = member;
        this.item = item;
        this.count = count;
        this.totalPrice = item.getPrice() * count;
    }

    public void addCount(int count){
        this.count += count;
    }
}
