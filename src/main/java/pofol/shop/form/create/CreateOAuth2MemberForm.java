package pofol.shop.form.create;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.enums.Sex;

import javax.validation.constraints.NotEmpty;

/**
 * Oauth2 회원가입 시 필요한 데이터 폼 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-13
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-13
 */
@Data
@NoArgsConstructor
public class CreateOAuth2MemberForm {
    @NotEmpty(message = "회원이름은 필수입니다")
    private String userName; //회원명
    private String email; //이메일
    @NotEmpty(message = "실명은 필수입니다")
    private String realName; //실명
    private int age; //나이
    private Sex sex; //성별
    @NotEmpty(message = "기본 주소는 필수입니다")
    private String address1; //기본 주소
    private String address2; //상세 주소
    private int zipcode; //우편번호

    @Builder
    public CreateOAuth2MemberForm(String userName, String email, String realName,
                                  int age, Sex sex,
                                  String address1, String address2, int zipcode) {
        this.userName = userName;
        this.email = email;
        this.realName = realName;
        this.age = age;
        this.sex = sex;
        this.address1 = address1;
        this.address2 = address2;
        this.zipcode = zipcode;
    }
}
