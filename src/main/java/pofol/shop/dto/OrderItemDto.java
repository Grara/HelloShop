package pofol.shop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Item;

@Data
@NoArgsConstructor
public class OrderItemDto {
    private Long cartId;
    private Long itemId;
    private String itemName;
    private int price;
    private int count;
    private int totalPrice;

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
