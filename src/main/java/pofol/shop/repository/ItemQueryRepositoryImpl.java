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

import javax.persistence.EntityManager;
import java.util.List;

import static pofol.shop.domain.QItem.item;

public class ItemQueryRepositoryImpl implements ItemQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ItemQueryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ItemDto> searchWithPage(ItemSearchCondition condition, Pageable pageable) {
        //회원 목록용 쿼리
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

    private BooleanExpression itemNameContains(String itemName) { //조건에 있는 회원명의 포함 여부
        return StringUtils.hasText(itemName) ? item.itemName.contains(itemName) : null;
    }

    private BooleanExpression authorContains(String author) {
        return StringUtils.hasText(author) ? item.author.contains(author) : null;
    }

    private BooleanExpression priceGoe(Integer minPrice) {
        return item.price.goe(minPrice);
    }

    private BooleanExpression priceLoe(Integer maxPrice) {
        if(maxPrice == 0) return null;
        return item.price.loe(maxPrice);
    }

    private OrderSpecifier<?> sortOption(ItemSortOption option){

        switch (option){
            case NONE:
                return new OrderSpecifier(Order.DESC, item.id);
            case TOTAL_SALES:
                return new OrderSpecifier(Order.DESC, item.totalSales);
            case RATING:
                return new OrderSpecifier(Order.DESC, item.ratingAverage);
        }

        return null;
    }
}
