package pofol.shop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 주문아이템에 대한 엔티티 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-24
 */
@Entity
@Data
@NoArgsConstructor
public class OrderItem extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "orderitem_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //단방향 다대일
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY) //단방향 다대일
    @JoinColumn(name = "order_id")
    private Order order;

    private int count;
    private int totalPrice;
    //----------필드 끝 / 생성자 시작----------//
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

    //----------생서자 끝 / 메소드 시작----------//

    /**
     * 주문이 취소되었을 때, 상품의 재고를 복구시킵니다.
     *
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-24
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-24
     */
    public void cancel(){
        this.item.addQty(this.count);
    }
}
