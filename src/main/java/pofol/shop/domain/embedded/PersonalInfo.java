package pofol.shop.domain.embedded;

import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.enums.Sex;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@NoArgsConstructor
@Data
public class PersonalInfo {
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
