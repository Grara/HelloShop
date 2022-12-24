package pofol.shop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import pofol.shop.domain.Order;
import pofol.shop.domain.enums.OrderStatus;
import pofol.shop.dto.business.OrderDto;
import pofol.shop.dto.business.OrderSearchCondition;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static pofol.shop.domain.QOrder.order;
import static pofol.shop.domain.QMember.member;

/**
 * 주문 관련 DB작업 중 복잡한 쿼리를 수행하는 메소드를 구현한 DAO 클래스입니다. <br/>
 * OrderRepository 객체를 통해서 메소드를 사용할 수 있습니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-01
 */
public class OrderQueryRepositoryImpl implements OrderQueryRepository{

    private final JPAQueryFactory queryFactory;

    public OrderQueryRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Order> search(OrderSearchCondition condition) {
        return queryFactory.select(order)
                .from(order)
                .leftJoin(order.member, member).fetchJoin()
                .where(
                        statusEq(condition.getOrderStatus()),
                        userNameContains(condition.getUserName()),
                        dateLoe(condition.getEndDate()),
                        dateGoe(condition.getStartDate())
                )
                .fetch();
    }

    /**
     * 검색 조건에 맞춰서 Order를 가져온 후 OrderDto로 변환시켜줍니다. <br/>
     * 이후 OrderDto리스트와 페이징 정보를 반환합니다.
     *
     * @param condition 검색조건
     * @param pageable  페이징 정보
     * @return OrderDto리스트와 페이징 정보를 함께 담고있는 PageImpl객체
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-01
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-01
     */
    @Override
    public Page<OrderDto> searchWithPage(OrderSearchCondition condition, Pageable pageable) {
        //실제 주문 목록용 쿼리
        List<Order> content = queryFactory
                .selectFrom(order)
                .leftJoin(order.member, member).fetchJoin()
                .where(
                        statusEq(condition.getOrderStatus()),
                        userNameContains(condition.getUserName()),
                        dateGoe(condition.getStartDate()),
                        dateLoe(condition.getEndDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //카운트 쿼리
        Long total = queryFactory
                .select(order.count())
                .from(order)
                .leftJoin(order.member, member)
                .where(
                        statusEq(condition.getOrderStatus()),
                        userNameContains(condition.getUserName()),
                        dateGoe(condition.getStartDate()),
                        dateLoe(condition.getEndDate())
                )
                .fetchOne();

        //주문을 dto로 변환
        List<OrderDto> result = content.stream().map(OrderDto::new).collect(Collectors.toList());

        return new PageImpl<>(result, pageable, total);
    }


    private BooleanExpression statusEq(OrderStatus status) { //주문 상태
        if(status == null) return null;
        return order.status.eq(status);
    }
    private BooleanExpression userNameContains(String userName){ //조건에 있는 회원명 포함 여부
        return  StringUtils.hasText(userName) ? member.userName.contains(userName) : null;
    }
    private BooleanExpression dateGoe(LocalDateTime startDate){ //시작 날짜
        if(startDate == null) return null;
        return order.orderDate.goe(startDate);
    }
    private BooleanExpression dateLoe(LocalDateTime endDate){ //종료 날짜
        if(endDate == null) return null;
        return order.orderDate.loe(endDate);
    }

}
