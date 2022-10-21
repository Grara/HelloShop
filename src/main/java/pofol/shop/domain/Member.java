package pofol.shop.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.embedded.PersonalInfo;

import javax.persistence.*;


@Data
@Entity
@NoArgsConstructor
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String userName;

    @Embedded
    private Address address;

    @Embedded
    private PersonalInfo personalInfo;

    public Member(String userName, PersonalInfo personalInfo){
        this.userName = userName;
        this.personalInfo = personalInfo;
    }
}
