package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pofol.shop.formAndDto.MemberForm;
import pofol.shop.domain.Member;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.embedded.PersonalInfo;
import pofol.shop.domain.enums.Role;
import pofol.shop.service.MemberService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/members")
    public String list(Model model)throws Exception{
        model.addAttribute("members", memberService.findList());
        return "members/memberList";
    }

    @GetMapping("/members/new")
    public String createForm(Model model)throws Exception{
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result)throws Exception{
        //비밀번호와 비밀번호 확인이 일치하는지 체크
        if(!form.getPassword().equals(form.getPasswordCheck()))
            result.addError(new FieldError("memberForm",
                    "passwordCheck",
                    "패스워드와 패스워드확인이 일치하지 않습니다"));
        
        //값 입력에 문제가 있으면 다시 수정하도록함
        if(result.hasErrors()){
            return "members/createMemberForm";
        }

        Member member = new Member();
        Address address = new Address(form.getAddress1(), form.getAddress2(), form.getZipcode());
        PersonalInfo personalInfo = new PersonalInfo(form.getRealName(), form.getAge(), form.getSex());
        member.setUserName(form.getUserName());
        member.setPassword(passwordEncoder.encode(form.getPassword()));
        member.setAddress(address);
        member.setPersonalInfo(personalInfo);
        member.setRole(Role.ROLE_USER);

        memberService.signUp(member);
        return "redirect:/";
    }
}
