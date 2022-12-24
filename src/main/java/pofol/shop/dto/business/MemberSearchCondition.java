package pofol.shop.dto.business;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 검색 시 조건을 지정할 때 사용되는 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-30
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-30
 */
@Data
@NoArgsConstructor
public class MemberSearchCondition {
    private String userName; //회원명
    private String realName; //실명
}
