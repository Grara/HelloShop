package pofol.shop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.StringUtils;
import pofol.shop.domain.Member;
import pofol.shop.domain.Order;
import pofol.shop.domain.QOrder;
import pofol.shop.domain.enums.OrderStatus;
import pofol.shop.dto.OrderSearchCondition;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
                        userNameEq(condition.getUserName()),
                        dateLoe(condition.getEndDate()),
                        dateGoe(condition.getStartDate())
                )
                .fetch();
    }

    private BooleanExpression statusEq(OrderStatus status) { //주문 상태
        if(status == null) return null;
        return order.status.eq(status);
    }
    private BooleanExpression userNameEq(String userName){ //주문한 회원명
        return  StringUtils.hasText(userName) ? member.userName.eq(userName) : null;
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
