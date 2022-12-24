package pofol.shop.form.update;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import pofol.shop.form.UserNameRequiredForm;

/**
 * 이미지변경 시 필요한 데이터 폼 클래스입니다. <br>
 * ex) 프로필 이미지, 상품 이미지
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-30
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-30
 */
@Data
public class UpdateImageForm extends UserNameRequiredForm {
    private MultipartFile uploadImg;
}
