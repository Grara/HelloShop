package pofol.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pofol.shop.domain.Member;
import pofol.shop.dto.business.MemberDto;
import pofol.shop.dto.business.MemberSearchCondition;

import java.util.List;

/**
 * MemberQueryRepositoryImpl을 만들기 위한 인터페이스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-30
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-30
 */
public interface MemberQueryRepository {

    List<Member> search(MemberSearchCondition condition);

    Page<MemberDto> searchWithPage(MemberSearchCondition condition, Pageable pageable);
}
