package pofol.shop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemSearchCondition {
    private String itemName;
    private String author;
    private int minPrice;
    private int maxPrice;
    private ItemSortOption sortOption = ItemSortOption.NONE;

}
