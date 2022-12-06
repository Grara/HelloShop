package pofol.shop.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemSortOption {
    NONE("최신순"),
    TOTAL_SALES("판매량순"),
    RATING("평점순");

    private final String value;
}
