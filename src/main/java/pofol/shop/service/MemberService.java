package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.Member;
import pofol.shop.repository.MemberRepository;
import pofol.shop.repository.MemberRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Long join(Member member){
        duplicateCheck(member);
        memberRepository.save(member);
        return member.getId();
    }

    public void duplicateCheck(Member member){
        List<Member> findMembers = memberRepository.findMemberByUserName(member.getUserName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원임미다");
        }
    }

    public void delete(Member member){
        memberRepository.delete(member);
    }
}
