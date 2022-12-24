package pofol.shop.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 로그인 시 사용되는 데이터 폼 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-13
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-13
 */
@Data
public class LoginForm {
    @NotEmpty(message = "회원명을 입력하세요")
    private String username;
    @NotEmpty(message = "비밀번호를 입력하세요")
    private String password;
}
