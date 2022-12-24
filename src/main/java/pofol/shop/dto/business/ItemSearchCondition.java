package pofol.shop.dto.business;

import lombok.Data;

/**
 * 상품을 검색 시 조건을 지정할 때 사용되는 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-07
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-19
 */
@Data
public class ItemSearchCondition {
    private String itemName; //상품명
    private String author; //저자
    private Integer minPrice; //최소가격
    private Integer maxPrice; //최대가격
    private ItemSortOption sortOption = ItemSortOption.NONE; //정렬기준

    public void removeNull(){
        if(minPrice == null) minPrice = 0;
        if(maxPrice == null) maxPrice = 0;
    }
}
