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

    @PostMapping("/members/duplicateCheck")
    public boolean duplicateCheck(@RequestBody String userName) throws Exception {
        if(userName.equals("")||userName == null){
            return false;
        }
        if(!userName.matches("[a-zA-Z0-9]*$")){
            return false;
        }

        try {
            Member findMember = memberService.findOneByName(userName);
            return false;
        }catch (EntityNotFoundException e) {
            return true;
        }
    }
}
