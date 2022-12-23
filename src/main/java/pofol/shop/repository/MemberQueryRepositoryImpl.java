package pofol.shop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import pofol.shop.domain.Member;
import pofol.shop.dto.business.MemberDto;
import pofol.shop.dto.business.MemberSearchCondition;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public Page<MemberDto> searchWithPage(MemberSearchCondition condition, Pageable pageable) {
        //회원 목록용 쿼리
        List<Member> content = queryFactory
                .selectFrom(member)
                .where(
                        userNameContains(condition.getUserName()),
                        realNameContains(condition.getRealName())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //카운트 쿼리
        Long total = queryFactory
                .select(member.count())
                .from(member)
                .where(
                        userNameContains(condition.getUserName()),
                        realNameContains(condition.getRealName())
                )
                .fetchOne();

        //엔티티를 dto로 변환
        List<MemberDto> result = content.stream().map(MemberDto::new).collect(Collectors.toList());

        return new PageImpl<>(result, pageable, total);
    }

    private BooleanExpression userNameContains(String userName) { //조건에 있는 회원명의 포함 여부
        return StringUtils.hasText(userName) ? member.userName.contains(userName) : null;
    }

    private BooleanExpression realNameContains(String realName) { //조건에 있는 실제이름 포함 여부
        return StringUtils.hasText(realName) ? member.personalInfo.realName.contains(realName) : null;
    }

}
