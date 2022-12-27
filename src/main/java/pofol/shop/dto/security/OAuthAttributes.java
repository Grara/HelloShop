package pofol.shop.dto.security;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;
import pofol.shop.domain.Member;
import pofol.shop.domain.enums.Role;

import java.util.Map;

/**
 * OAuth2 로그인 시 속성으로 사용되는 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-08
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-12
 */
@Getter
@ToString
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    /**
     * 로그인한 포털에 따라 속성을 설정해서 반환해줍니다.
     *
     * @param registrationId 포털 이름
     * @param userNameAttributeName 유저의 id값의 key
     * @param attributes OAuth로그인 시 전달받은 속성값
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-08
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-12
     */
    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes){
        if(registrationId.equals("google")) {
            return ofGoogle(userNameAttributeName, attributes);
        }


        return ofNaver(userNameAttributeName, attributes);

    }

    /**
     * 네이버 로그인 시 속성을 설정해서 반환해줍니다.
     *
     * @param userNameAttributeName 유저의 id값의 key
     * @param attributes OAuth로그인 시 전달받은 속성값
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-12
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-12
     */
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {

        //key를 response해서 찾아야 됨
        Map<String, Object> res = (Map<String, Object>) attributes.get(userNameAttributeName);
        return OAuthAttributes.builder()
                .name((String) res.get("name"))
                .email((String) res.get("email"))
                .attributes(res)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /**
     * 구글 로그인 시 속성을 설정해서 반환해줍니다.
     *
     * @param userNameAttributeName 유저의 id값의 key
     * @param attributes OAuth로그인 시 전달받은 속성값
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-08
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-08
     */
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /**
     * 현재 속성을 바탕으로 새로운 Member객체를 생성해서 반환합니다.
     *
     * @param encoder 사용할 PasswordEncoder
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-08
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-08
     */
    public Member toEntity(PasswordEncoder encoder) {
        return Member.builder()
                .userName(name)
                .email(email)
                .password(encoder.encode("1234"))
                .role(Role.ROLE_GUEST)
                .build();
    }
}
