package pofol.shop.form.create;


import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.form.UserNameRequiredForm;

/**
 * 장바구니 아이템 생성 시 필요한 데이터 폼 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-09
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-09
 */
@Data
@NoArgsConstructor
public class CreateCartForm extends UserNameRequiredForm {
    private Long itemId; //상품 id
    private Integer count; // 수량
}
