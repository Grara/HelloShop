package pofol.shop.form;

import lombok.Data;

/**
 * 대상이 되는 유저와 현재 로그인 세션의 일치가 반드시 필요한 Form들이 상속받는 클래스입니다.
 * 인증관련 AOP를 적용할 때 사용됩니다. AuthAop 참조
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-24
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-24
 */
@Data
public abstract class UserNameRequiredForm {
    private String userName;
}
