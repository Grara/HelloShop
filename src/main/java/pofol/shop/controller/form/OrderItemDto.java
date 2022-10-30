package pofol.shop.controller.form;

import lombok.Data;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Item;

@Data
public class OrderItemDto {
    private Long cartId;
    private Long itemId;
    private String itemName;
    private int price;
    private int count;

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
    }
}
