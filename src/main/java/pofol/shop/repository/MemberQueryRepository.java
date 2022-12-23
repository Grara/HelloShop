package pofol.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pofol.shop.domain.Member;
import pofol.shop.dto.business.MemberDto;
import pofol.shop.dto.business.MemberSearchCondition;

import java.util.List;

public interface MemberQueryRepository {

    List<Member> search(MemberSearchCondition condition);

    Page<MemberDto> searchWithPage(MemberSearchCondition condition, Pageable pageable);
}
