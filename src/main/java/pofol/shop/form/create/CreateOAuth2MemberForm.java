package pofol.shop.form.create;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.enums.Sex;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class CreateOAuth2MemberForm {
    @NotEmpty(message = "회원이름은 필수입니다")
    private String userName;
    private String email;
    @NotEmpty(message = "실명은 필수입니다")
    private String realName;
    private int age;
    private Sex sex;
    @NotEmpty(message = "기본 주소는 필수입니다")
    private String address1;
    private String address2;
    private int zipcode;

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