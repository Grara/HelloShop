package pofol.shop.domain.embedded;

import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.enums.Sex;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * 개인 신상정보를 나타내는 임베디드 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2023-01-02
 */
@Embeddable
@NoArgsConstructor
@Data
public class PersonalInfo implements Serializable {
    private String realName;
    @Column(nullable = true)
    private int age;
    @Enumerated(EnumType.STRING)
    private Sex sex;

    public PersonalInfo(String realName, int age){
        this.realName = realName;
        this.age = age;
        this.sex = Sex.MALE;
    }

    public PersonalInfo(String realName, int age, Sex sex) {
        this.realName = realName;
        this.age = age;
        this.sex = sex;
    }

}
