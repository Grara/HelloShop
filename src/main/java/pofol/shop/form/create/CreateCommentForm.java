package pofol.shop.form.create;

import lombok.Data;
import pofol.shop.form.UserNameRequiredForm;

/**
 * 상품 후기 생성 시 필요한 데이터 폼 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-24
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-10-24
 */
@Data
public class CreateCommentForm extends UserNameRequiredForm {
    private Long itemId; //상품 id
    private String content; //후기 내용
    private int rating; //평점
}
