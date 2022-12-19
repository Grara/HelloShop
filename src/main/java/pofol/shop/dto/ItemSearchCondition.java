package pofol.shop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ItemSearchCondition {
    private String itemName;
    private String author;
    private Integer minPrice;
    private Integer maxPrice;
    private ItemSortOption sortOption = ItemSortOption.NONE;

    public void removeNull(){
        if(minPrice == null) minPrice = 0;
        if(maxPrice == null) maxPrice = 0;
    }
}
