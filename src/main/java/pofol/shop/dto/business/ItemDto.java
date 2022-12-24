package pofol.shop.dto.business;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.Item;

/**
 * 상품의 DTO클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-07
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-07
 */
@Data
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private String name; //상품명
    private String author; //저자
    private String descriptionTitle; //설명 제목
    private String description; //설명 본문
    private int price; //가격
    private int quantity; //재고
    private float rating; //평균 평점

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

    @QueryProjection //쿼리 dsl을 사용하기위한 생성자
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
