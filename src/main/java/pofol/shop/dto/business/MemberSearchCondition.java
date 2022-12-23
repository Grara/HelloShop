package pofol.shop.dto.business;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberSearchCondition {
    private String userName;
    private String realName;
}
