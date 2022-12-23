package pofol.shop.dto.security;


import lombok.Getter;
import lombok.ToString;
import pofol.shop.dto.business.MemberDto;

import java.util.Map;

@Getter
@ToString
public class UserAdapter extends CustomUserDetails {
    private MemberDto member;
    private Map<String,Object> attributes;

    public UserAdapter(MemberDto member) {
        super(member);
        this.member = member;
    }

    public UserAdapter(MemberDto member, Map<String, Object> attributes) {
        super(member, attributes);
        this.member = member;
        this.attributes = attributes;
    }
}
