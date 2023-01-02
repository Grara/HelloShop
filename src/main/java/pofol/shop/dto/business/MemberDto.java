package pofol.shop.dto.business;

import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.Member;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.embedded.PersonalInfo;
import pofol.shop.domain.enums.Role;

import java.io.Serializable;

/**
 * 회원에 대한 DTO클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-20
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2023-01-02
 */
@Data
@NoArgsConstructor
public class MemberDto implements Serializable {
    private Long id;
    private String userName;
    private String password;
    private String email;
    private Address address;
    private PersonalInfo personalInfo;
    private Role role;

    public MemberDto(Member member){
        this.id = member.getId();
        this.userName = member.getUserName();
        this.password = member.getPassword();
        this.email = member.getEmail();
        this.address = member.getAddress();
        this.personalInfo = member.getPersonalInfo();
        this.role = member.getRole();
    }
}
