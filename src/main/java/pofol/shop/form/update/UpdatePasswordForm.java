package pofol.shop.form.update;


import lombok.Data;
import pofol.shop.form.UserNameRequiredForm;

import javax.validation.constraints.NotEmpty;

/**
 * 비밀번호를 변경할 때 필요한 데이터 폼 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-22
 */
@Data
public class UpdatePasswordForm extends UserNameRequiredForm {
    @NotEmpty(message = "기존 비밀번호를 입력해주세요")
    private String curPassword;
    @NotEmpty(message = "새로운 비밀번호를 입력해주세요")
    private String newPassword;
    @NotEmpty(message = "비밀번호 확인을 입력해주세요")
    private String newPasswordCheck;
}
