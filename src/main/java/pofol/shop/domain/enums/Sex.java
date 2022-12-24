package pofol.shop.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 성별을 나타내는 enum클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-13
 */
@Getter
@RequiredArgsConstructor
public enum Sex {
    MALE("남성"),
    FEMALE("여성");

    private final String value;
}
