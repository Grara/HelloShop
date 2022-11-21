package pofol.shop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import pofol.shop.domain.Member;
import pofol.shop.domain.Order;
import pofol.shop.domain.QOrder;
import pofol.shop.domain.enums.OrderStatus;
import pofol.shop.dto.OrderSearchCondition;

import javax.persistence.EntityManager;
import java.util.List;

import static pofol.shop.domain.QOrder.order;

public class OrderQueryRepositoryImpl implements OrderQueryRepository{

    private final JPAQueryFactory queryFactory;

    public OrderQueryRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Order> search(OrderSearchCondition condition) {
        return queryFactory.select(order)
                .from(order)
                .where(
                        statusEq(condition.getOrderStatus()),
                        memberEq(condition.getMember())
                )
                .fetch();

    }

    private BooleanExpression statusEq(OrderStatus status) {
        if(status == null) return null;
        else return order.status.eq(status);
    }
    private BooleanExpression memberEq(Member member){
        if(member == null) return null;
        else return order.member.eq(member);
    }
}
