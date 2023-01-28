package pofol.shop.domain;


import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;

/**
 * 장바구니에 담은 아이템을 나타내는 엔티티 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-30
 */
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue
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
    @Builder
    public Cart(Member member, Item item, int count) {
        this.member = member;
        this.item = item;
        this.count = count;
        this.totalPrice = item.getPrice() * count;
    }

    //----------생성자 끝 / 메소드 시작----------//

    /**
     * 현재 객체에 수량을 더합니다.
     *
     * @param count 더할 수량
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-05
     */
    public void addCount(int count) {
        this.count += count;
    }
}
