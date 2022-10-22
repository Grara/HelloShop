package pofol.shop.controller.form;

import lombok.Data;
import pofol.shop.domain.enums.Sex;

import javax.validation.constraints.NotEmpty;

@Data
public class MemberForm {

    @NotEmpty(message = "회원이름은 필수입니다")
    private String userName;
    @NotEmpty(message = "비밀번호는 필수입니다")
    private String password;
    private String passwordCheck;
    @NotEmpty(message = "실명은 필수입니다")
    private String realName;
    private int age;
    private Sex sex;
    @NotEmpty(message = "기본 주소는 필수입니다")
    private String address1;
    private String address2;
    private int zipcode;
}
