package pofol.shop.dto.business;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 상품을 검색 시 검색조건에 정렬순서를 지정하는 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-07
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-19
 */
@Getter
@RequiredArgsConstructor
public enum ItemSortOption {
    NONE("최신순"),
    TOTAL_SALES("판매량순"),
    RATING("평점순");

    private final String value;
}
