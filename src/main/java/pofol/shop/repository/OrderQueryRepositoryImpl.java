package pofol.shop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import pofol.shop.domain.Order;
import pofol.shop.domain.enums.OrderStatus;
import pofol.shop.dto.OrderDto;
import pofol.shop.dto.OrderSearchCondition;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static pofol.shop.domain.QOrder.order;
import static pofol.shop.domain.QMember.member;

public class OrderQueryRepositoryImpl implements OrderQueryRepository{

    private final JPAQueryFactory queryFactory;

    public OrderQueryRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Order> search(OrderSearchCondition condition) {
        return queryFactory.select(order)
                .from(order)
                .leftJoin(order.member, member)
                .where(
                        statusEq(condition.getOrderStatus()),
                        userNameContains(condition.getUserName()),
                        dateLoe(condition.getEndDate()),
                        dateGoe(condition.getStartDate())
                )
                .fetch();
    }

    @Override
    public Page<OrderDto> searchWithPage(OrderSearchCondition condition, Pageable pageable) {
        //실제 주문 목록용 쿼리
        List<Order> content = queryFactory
                .selectFrom(order)
                .leftJoin(order.member, member)
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
