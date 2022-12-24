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

/**
 * 회원 관련 DB작업 중 복잡한 쿼리를 수행하는 메소드를 구현한 DAO 클래스입니다. <br/>
 * MemberRepository 객체를 통해서 메소드를 사용할 수 있습니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-30
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-01
 */
public class MemberQueryRepositoryImpl implements MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    public MemberQueryRepositoryImpl(EntityManager em) {
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

    /**
     * 검색 조건에 맞춰서 Member를 가져온 후 MemberDto로 변환시켜줍니다. <br/>
     * 이후 MemberDto리스트와 페이징 정보를 반환합니다.
     *
     * @param condition 검색조건
     * @param pageable  페이징 정보
     * @return MemberDto리스트와 페이징 정보를 함께 담고있는 PageImpl객체
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-01
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-01
     */
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
