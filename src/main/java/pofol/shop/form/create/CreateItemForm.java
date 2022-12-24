package pofol.shop.form.create;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import pofol.shop.form.UserNameRequiredForm;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * 상품 생성 시 필요한 데이터 폼 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-10-21
 */
@Data
@NoArgsConstructor
public class CreateItemForm extends UserNameRequiredForm {
    private Long id; //상품 id
    @NotEmpty(message = "이름은 필수입니다")
    private String itemName; //상품명
    @NotEmpty(message = "저자는 필수입니다")
    private String author; //저자
    private String descriptionTitle; //설명 제목
    private String description; //설명 본문
    @Min(value = 0, message = "가격은 0이상이어야 합니다")
    private int price; //가격
    @Min(value = 1, message = "수량은 1개 이상이어야 합니다")
    private int quantity; //재고수량
    private MultipartFile thumbnail; //상품 이미지

    @Builder
    public CreateItemForm(Long id, String itemName, String author, String descriptionTitle,
                          String description, int price, int quantity, MultipartFile thumbnail) {
        this.id = id;
        this.itemName = itemName;
        this.author = author;
        this.descriptionTitle = descriptionTitle;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.thumbnail = thumbnail;
    }
}
