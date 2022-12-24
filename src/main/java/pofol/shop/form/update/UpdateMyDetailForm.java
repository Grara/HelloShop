package pofol.shop.form.update;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import pofol.shop.domain.Member;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.embedded.PersonalInfo;
import pofol.shop.domain.enums.Sex;
import pofol.shop.form.UserNameRequiredForm;

import javax.validation.constraints.NotEmpty;

/**
 * 개인 세부정보를 수정할 떄 필요한 데이터 폼 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-20
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-20
 */
@Data
@NoArgsConstructor
public class UpdateMyDetailForm extends UserNameRequiredForm {
    @NotEmpty(message = "기본주소는 필수입니다.")
    private String address1; //기본주소
    private String address2; //상세주소
    private int zipcode; //우편번호
    private String realName; //실명
    private int age; //나이
    private Sex sex; //성별
    private Long profileId; //프로일 이미지 FileEntity id값

    public UpdateMyDetailForm(Member member){
        setUserName(member.getUserName());
        this.address1 = member.getAddress().getAddress1();
        this.address2 = member.getAddress().getAddress2();
        this.zipcode = member.getAddress().getZipcode();
        this.realName = member.getPersonalInfo().getRealName();
        this.age = member.getPersonalInfo().getAge();
        this.sex = member.getPersonalInfo().getSex();
    }
}
