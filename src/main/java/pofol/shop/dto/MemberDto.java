package pofol.shop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.Member;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.embedded.PersonalInfo;

@Data
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String userName;
    private Address address;
    private PersonalInfo personalInfo;

    public MemberDto(Member member){
        this.id = member.getId();
        this.userName = member.getUserName();
        this.address = member.getAddress();
        this.personalInfo = member.getPersonalInfo();
    }
}
