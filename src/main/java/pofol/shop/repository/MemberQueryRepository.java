package pofol.shop.repository;

import pofol.shop.domain.Member;
import pofol.shop.dto.MemberSearchCondition;

import java.util.List;

public interface MemberQueryRepository {

    List<Member> search(MemberSearchCondition condition);
}
