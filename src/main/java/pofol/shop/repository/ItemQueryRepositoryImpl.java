package pofol.shop.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import pofol.shop.dto.*;
import pofol.shop.dto.business.ItemDto;
import pofol.shop.dto.business.ItemSearchCondition;
import pofol.shop.dto.business.ItemSortOption;
import pofol.shop.dto.business.QItemDto;

import javax.persistence.EntityManager;
import java.util.List;

import static pofol.shop.domain.QItem.item;

/**
 * 상품 관련 DB작업 중 복잡한 쿼리를 수행하는 메소드를 구현한 DAO 클래스입니다. <br/>
 * ItemRepository 객체를 통해서 메소드를 사용할 수 있습니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-07
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-07
 */
public class ItemQueryRepositoryImpl implements ItemQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ItemQueryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    /**
     * 검색 조건에 맞춰서 Item을 가져온 후 ItemDto로 변환시켜줍니다. <br/>
     * 이후 ItemDto리스트와 페이징 정보를 반환합니다.
     *
     * @param condition 검색조건
     * @param pageable  페이징 정보
     * @return ItemDto리스트와 페이징 정보를 함께 담고있는 PageImpl객체
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-07
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-07
     */
    @Override
    public Page<ItemDto> searchWithPage(ItemSearchCondition condition, Pageable pageable) {
        //상품 목록용 쿼리
        List<ItemDto> content = queryFactory
                .select(new QItemDto(
                        item.id,
                        item.itemName,
                        item.author,
                        item.descriptionTitle,
                        item.description,
                        item.price,
                        item.quantity,
                        item.ratingAverage
                ))
                .from(item)
                .where(
                        itemNameContains(condition.getItemName()),
                        authorContains(condition.getAuthor()),
                        priceGoe(condition.getMinPrice()),
                        priceLoe(condition.getMaxPrice())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sortOption(condition.getSortOption()))
                .fetch();

        //카운트 쿼리
        Long total = queryFactory
                .select(item.count())
                .from(item)
                .where(
                        itemNameContains(condition.getItemName()),
                        authorContains(condition.getAuthor()),
                        priceGoe(condition.getMinPrice()),
                        priceLoe(condition.getMaxPrice())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression itemNameContains(String itemName) { //검색조건에 있는 회원명의 포함 여부
        return StringUtils.hasText(itemName) ? item.itemName.contains(itemName) : null;
    }

    private BooleanExpression authorContains(String author) { //검색조건에 있는 저자이름의 포함 여부
        return StringUtils.hasText(author) ? item.author.contains(author) : null;
    }

    private BooleanExpression priceGoe(Integer minPrice) { //검색조건에 있는 최소 가격 이상인지
        return item.price.goe(minPrice);
    }

    private BooleanExpression priceLoe(Integer maxPrice) { //검색조건에 있는 최대 가격 이하인지
        if (maxPrice == 0) return null;
        return item.price.loe(maxPrice);
    }

    private OrderSpecifier<?> sortOption(ItemSortOption option) { //조건의 정렬기준에 따라 정렬

        switch (option) {
            case NONE: //최신 등록순
                return new OrderSpecifier(Order.DESC, item.id);
            case TOTAL_SALES: //판매량 순
                return new OrderSpecifier(Order.DESC, item.totalSales);
            case RATING: //평점 순
                return new OrderSpecifier(Order.DESC, item.ratingAverage);
        }
        return null;
    }
}
