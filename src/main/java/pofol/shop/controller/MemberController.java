package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pofol.shop.controller.form.MemberForm;
import pofol.shop.domain.Member;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.embedded.PersonalInfo;
import pofol.shop.service.MemberService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members")
    public String list(Model model){
        model.addAttribute("members", memberService.findMembers());
        return "members/memberList";
    }

    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result){
        if(result.hasErrors()){
            return "members/createMemberForm";
        }

        Member member = new Member();
        Address address = new Address(form.getAddress1(), form.getAddress2(), form.getZipcode());
        PersonalInfo personalInfo = new PersonalInfo(form.getRealName(), form.getAge(), form.getSex());
        member.setUserName(form.getUserName());
        member.setAddress(address);
        member.setPersonalInfo(personalInfo);

        memberService.join(member);
        return "redirect:/members";
    }
}
