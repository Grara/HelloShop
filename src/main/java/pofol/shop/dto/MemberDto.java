package pofol.shop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.embedded.PersonalInfo;

@Data
@NoArgsConstructor
public class MemberDto {
    private String userName;
    private Address address;
    private PersonalInfo personalInfo;
}
