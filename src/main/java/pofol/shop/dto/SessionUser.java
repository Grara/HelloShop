package pofol.shop.dto;


import lombok.Getter;
import pofol.shop.domain.Member;

@Getter
public class SessionUser {
    private String username;
    private String email;

    public SessionUser(Member member) {
        this.username = member.getUserName();
        this.email = member.getEmail();
    }
}
