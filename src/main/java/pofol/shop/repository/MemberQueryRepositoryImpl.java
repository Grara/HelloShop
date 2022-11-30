package pofol.shop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.StringUtils;
import pofol.shop.domain.Member;
import pofol.shop.domain.QMember;
import pofol.shop.dto.MemberSearchCondition;

import javax.persistence.EntityManager;
import java.util.List;

import static pofol.shop.domain.QMember.member;

public class MemberQueryRepositoryImpl implements MemberQueryRepository{

    private final JPAQueryFactory queryFactory;

    public MemberQueryRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }



    @Override
    public List<Member> search(MemberSearchCondition condition) {
        return queryFactory.select(member)
                .from(member)
                .where(
                        userNameContains(condition.getUserName()),
                        realNameContains(condition.getRealName())
                )
                .fetch();
    }

    private BooleanExpression userNameContains(String userName) {
        return StringUtils.hasText(userName) ? member.userName.contains(userName) : null;
    }

    private BooleanExpression realNameContains(String realName) {
        return StringUtils.hasText(realName) ? member.personalInfo.realName.contains(realName) : null;
    }

}
