package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pofol.shop.config.DefaultValue;
import pofol.shop.form.create.CreateMemberForm;
import pofol.shop.domain.Member;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.embedded.PersonalInfo;
import pofol.shop.domain.enums.Role;
import pofol.shop.form.update.UpdateImageForm;
import pofol.shop.form.update.UpdateMyDetailForm;
import pofol.shop.form.update.UpdatePasswordForm;
import pofol.shop.service.FileService;
import pofol.shop.service.MemberService;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;

import static pofol.shop.config.DefaultValue.DEFAULT_PROFILE_IMAGE_ID;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/members") //전체 Member 목록
    public String list(Model model){
        model.addAttribute("members", memberService.findList());
        return "members/memberList";
    }

    @GetMapping("/members/new") //Member 가입 폼 화면
    public String createForm(Model model){
        model.addAttribute("createMemberForm", new CreateMemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new") //Member 가입 요청
    public String create(@Valid CreateMemberForm form, BindingResult result){

        //비밀번호와 비밀번호 확인이 일치하는지 체크
        if(!form.getPassword().equals(form.getPasswordCheck()))
            result.addError(new FieldError("createMemberForm",
                    "passwordCheck",
                    "패스워드와 패스워드확인이 일치하지 않습니다"));
        
        //값 입력에 문제가 있으면 다시 수정하도록함
        if(result.hasErrors()){
            return "members/createMemberForm";
        }

        try {
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
        }catch(Exception e){
            return "errors/unknownError";
        }
    }
}
