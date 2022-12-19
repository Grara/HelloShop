package pofol.shop.service;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;
import pofol.shop.domain.Member;
import pofol.shop.domain.enums.Role;

import java.util.Map;

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

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes){
        if(registrationId.equals("google")) {
            return ofGoogle(userNameAttributeName, attributes);
        }


        return ofNaver(userNameAttributeName, attributes);

    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        System.out.println(userNameAttributeName);
        System.out.println(attributes);
        Map<String, Object> res = (Map<String, Object>) attributes.get(userNameAttributeName);
        System.out.println(res);
        return OAuthAttributes.builder()
                .name((String) res.get("name"))
                .email((String) res.get("email"))
                .attributes(res)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        System.out.println(userNameAttributeName);
        System.out.println(attributes);
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    public Member toEntity(PasswordEncoder encoder) {
        return Member.builder()
                .userName(name)
                .email(email)
                .password(encoder.encode("1234"))
                .role(Role.ROLE_GUEST)
                .build();
    }

}
