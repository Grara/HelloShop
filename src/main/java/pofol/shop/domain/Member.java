package pofol.shop.domain;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.embedded.PersonalInfo;
import pofol.shop.domain.enums.Role;
import pofol.shop.domain.enums.Sex;

import javax.persistence.*;

/**
 * 회원에 대한 엔티티 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-08
 */
@Data
@Entity
@NoArgsConstructor
public class Member extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) //단방향 일대일
    @JoinColumn(name = "file_id")
    private FileEntity profileImage; //프로필 이미지 파일 엔티티

    private String userName;
    private String password;
    @Embedded
    private Address address;
    @Embedded
    private PersonalInfo personalInfo;
    @Enumerated(EnumType.STRING)
    private Role role; //권한
    private String email;

    //----------필드 끝 / 생성자 시작----------//
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
        this.email = "aaa";
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

    @Builder
    public Member(FileEntity profileImage, String userName, String password, Address address, PersonalInfo personalInfo, Role role, String email) {
        this.profileImage = profileImage;
        this.userName = userName;
        this.password = password;
        this.address = address;
        this.personalInfo = personalInfo;
        this.role = role;
        this.email = email;
    }
    //----------생성자 끝 / 메소드 시작----------//

    /**
     * 회원의 이름과 이메일을 수정합니다.
     *
     * @param userName 수정 후 적용할 회원명
     * @param email 수정 후 적용할 이메일
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-08
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-08
     */
    public Member update(String userName, String email){
        this.userName = userName;
        this.email = email;

        return this;
    }
}
