package pofol.shop.dto.business;

import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Item;

/**
 * 주문 아이템의 DTO클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-30
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-10-30
 */
@Data
@NoArgsConstructor
public class OrderItemDto {
    private Long cartId; //원본 장바구니 아이템 id, 주문아이템 생성 시 사용됨
    private Long itemId; //상품 id
    private String itemName; //상품명
    private int price; //상품 개당 가격
    private int count; //주문 수량
    private int totalPrice; //주문 아이템 총 가격

    public OrderItemDto(Item item) {
        this.itemId = item.getId();
        this.itemName = item.getItemName();
        this.price = item.getPrice();
    }

    public OrderItemDto(Cart cart){
        this.cartId = cart.getId();
        this.itemId = cart.getItem().getId();
        this.itemName = cart.getItem().getItemName();
        this.price = cart.getItem().getPrice();
        this.count = cart.getCount();
        this.totalPrice = price * count;
    }
}
