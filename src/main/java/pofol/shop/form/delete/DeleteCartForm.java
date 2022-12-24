package pofol.shop.form.delete;

import lombok.Data;
import pofol.shop.form.UserNameRequiredForm;

import java.util.ArrayList;
import java.util.List;

/**
 * 장바구니 아이템 삭제 시 필요한 데이터 폼 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-24
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-24
 */
@Data
public class DeleteCartForm extends UserNameRequiredForm {
    private List<Long> cartIds = new ArrayList<Long>();
}
