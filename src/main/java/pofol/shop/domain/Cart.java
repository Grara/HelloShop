package pofol.shop.domain;


import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Cart extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "cartitem_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //단방향 다대일
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY) //단방향 다대일
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    private int totalPrice;
    //----------필드 끝 / 생성자 시작----------//
    public Cart(Member member, Item item, int count) {
        this.member = member;
        this.item = item;
        this.count = count;
        this.totalPrice = item.getPrice() * count;
    }
    //----------생성자 끝 / 메소드 시작----------//
    public void addCount(int count){
        this.count += count;
    }

}
