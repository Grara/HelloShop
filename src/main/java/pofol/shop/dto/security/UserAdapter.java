package pofol.shop.dto.security;


import lombok.Getter;
import lombok.ToString;
import pofol.shop.dto.business.MemberDto;

import java.util.Map;

/**
 * CustomUserDetails을 상속받은 유저 세션 정보입니다. <br/>
 * Controller에서 실제로 사용하는 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-12
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-12
 */
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
