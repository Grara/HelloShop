package pofol.shop.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.embedded.PersonalInfo;
import pofol.shop.domain.enums.Role;
import pofol.shop.domain.enums.Sex;

import javax.persistence.*;


@Data
@Entity
@NoArgsConstructor
public class Member extends BaseEntity{
    //----------필드 시작----------//
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String userName;
    private String password;

    @Embedded
    private Address address;
    @Embedded
    private PersonalInfo personalInfo;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private FileEntity profileImage;

    //----------필드 끝----------//

    //----------생성자 시작----------//
    public Member(String userName, String password, Address address, PersonalInfo personalInfo) {
        this.userName = userName;
        this.password = password;
        this.address = address;
        this.personalInfo = personalInfo;
        this.role = Role.ROLE_USER;
    }

    public Member(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.address = new Address("서울", "신림", 1010);
        this.personalInfo = new PersonalInfo("노민준", 28, Sex.MALE);
        this.role = Role.ROLE_USER;
    }

    public Member(String userName, String password, Role role) {
        this.userName = userName;
        this.password = password;
        this.address = new Address("서울", "신림", 1010);
        this.personalInfo = new PersonalInfo("노민준", 28, Sex.MALE);
        this.role = role;
    }
    //----------생성자 끝----------//
}
