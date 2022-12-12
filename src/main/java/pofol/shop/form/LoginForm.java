package pofol.shop.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginForm {
    @NotEmpty(message = "회원명을 입력하세요")
    private String username;
    @NotEmpty(message = "비밀번호를 입력하세요")
    private String password;
}
