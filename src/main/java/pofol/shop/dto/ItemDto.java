package pofol.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.Item;

@Data
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String author;
    private String descriptionTitle;
    private String description;
    private int price;
    private int quantity;
    private float rating;

    public ItemDto(Item item) {
        this.id = item.getId();
        this.name = item.getItemName();
        this.author = item.getAuthor();
        this.descriptionTitle = item.getDescriptionTitle();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();
        this.rating = item.getRatingAverage();
    }

    @QueryProjection
    public ItemDto(Long id, String name, String author, String descriptionTitle, String description, int price, int quantity, float rating) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.descriptionTitle = descriptionTitle;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.rating = rating;
    }
}
