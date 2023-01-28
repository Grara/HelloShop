package pofol.shop.form.create;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.enums.Sex;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 일반 회원 가입 시 필요한 데이터 폼 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-12
 */
@Data
@NoArgsConstructor
public class CreateMemberForm {

    @NotEmpty(message = "회원이름은 필수입니다")
    private String userName; //회원명
    @NotEmpty(message = "비밀번호는 필수입니다")
    private String password; //비밀번호
    private String passwordCheck; //비밀번호 확인
    private String email; //이메일
    @NotEmpty(message = "실명은 필수입니다")
    private String realName; //실명
    private int age; //나이
    private Sex sex; //성별
    @NotEmpty(message = "기본 주소는 필수입니다")
    private String address1; //기본주소
    private String address2; //상세주소
    private int zipcode; //우편번호

    @Builder
    public CreateMemberForm(String userName, String password, String passwordCheck, String email,
                            String realName, int age, Sex sex, String address1, String address2,
                            int zipcode) {
        this.userName = userName;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.email = email;
        this.realName = realName;
        this.age = age;
        this.sex = sex;
        this.address1 = address1;
        this.address2 = address2;
        this.zipcode = zipcode;
    }
}
