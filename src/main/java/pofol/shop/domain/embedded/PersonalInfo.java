package pofol.shop.domain.embedded;

import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.enums.Sex;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@NoArgsConstructor
@Data
public class PersonalInfo {
    private String realName;
    private int age;
    @Enumerated(EnumType.STRING)
    private Sex sex;
    private int phoneNumber;
    private String email;

    public PersonalInfo(String realName, int age, Sex sex){
        this.realName = realName;
        this.age = age;
        this.sex = sex;
    }
}
