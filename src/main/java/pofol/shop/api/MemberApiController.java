package pofol.shop.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pofol.shop.domain.Member;
import pofol.shop.service.MemberService;

import javax.persistence.EntityNotFoundException;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/members/duplicateCheck") //Member가입 시 회원명 중복체크 버튼
    public boolean duplicateCheck(@RequestBody String userName){
        if(userName.equals("")||userName == null){
            return false;
        }

        //회원명이 영어와 숫자만으로 이루어졌는지 검사
        if(!userName.matches("[a-zA-Z0-9]*$")){
            return false;
        }

        //중복이 아니면 true를 반환해줘야됨
        return !memberService.isDuplicate(userName);
    }
}
