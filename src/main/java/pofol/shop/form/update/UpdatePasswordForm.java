package pofol.shop.form.update;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdatePasswordForm {
    @NotEmpty(message = "새로운 비밀번호를 입력해주세요")
    private String newPassword;
    @NotEmpty(message = "비밀번호 확인을 입력해주세요")
    private String newPasswordCheck;
}
