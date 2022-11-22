package pofol.shop.form.update;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import pofol.shop.domain.Member;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.embedded.PersonalInfo;
import pofol.shop.domain.enums.Sex;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class UpdateMyDetailForm {
    private String userName;
    @NotEmpty(message = "기본주소는 필수입니다.")
    private String address1;
    private String address2;
    private int zipcode;
    private String realName;
    private int age;
    private Sex sex;
    private Long profileId;

    public UpdateMyDetailForm(Member member){
        this.userName = member.getUserName();
        this.address1 = member.getAddress().getAddress1();
        this.address2 = member.getAddress().getAddress2();
        this.zipcode = member.getAddress().getZipcode();
        this.realName = member.getPersonalInfo().getRealName();
        this.age = member.getPersonalInfo().getAge();
        this.sex = member.getPersonalInfo().getSex();
    }
}
