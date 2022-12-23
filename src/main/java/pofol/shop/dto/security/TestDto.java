package pofol.shop.dto.security;

import lombok.Data;
import lombok.Getter;
import pofol.shop.domain.Member;

import java.io.Serializable;

@Getter
public class TestDto implements Serializable {
    private String name;

    public TestDto(String name) {
        this.name = name;
    }

    public TestDto(Member member){
        this.name = member.getUserName();
    }

}
