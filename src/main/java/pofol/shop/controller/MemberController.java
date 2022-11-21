package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pofol.shop.config.DefaultValue;
import pofol.shop.form.create.CreateMemberForm;
import pofol.shop.domain.Member;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.embedded.PersonalInfo;
import pofol.shop.domain.enums.Role;
import pofol.shop.form.update.UpdateMyDetailForm;
import pofol.shop.form.update.UpdatePasswordForm;
import pofol.shop.service.FileService;
import pofol.shop.service.MemberService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/members")
    public String list(Model model)throws Exception{
        model.addAttribute("members", memberService.findList());
        return "members/memberList";
    }

    @GetMapping("/members/new")
    public String createForm(Model model)throws Exception{
        model.addAttribute("createMemberForm", new CreateMemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid CreateMemberForm form, BindingResult result)throws Exception{
        //비밀번호와 비밀번호 확인이 일치하는지 체크
        if(!form.getPassword().equals(form.getPasswordCheck()))
            result.addError(new FieldError("createMemberForm",
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

    @GetMapping("/mypage")
    public String mypageHome(Model model, Principal principal) throws Exception{
        Member member = memberService.findOneByName(principal.getName());

        if(member.getProfileImage() != null){
            model.addAttribute("fileId", member.getProfileImage().getId());
        }

        model.addAttribute("userName", member.getUserName());

        return "/mypage/myhome";
    }

    @GetMapping("/mypage/details")
    public String mypageDetails(Model model, Principal principal) throws Exception{
        Member member = memberService.findOneByName(principal.getName());
        UpdateMyDetailForm form = new UpdateMyDetailForm(member);

        model.addAttribute("form", form);
        if(member.getProfileImage() != null){
            model.addAttribute("fileId", member.getProfileImage().getId());
        }

        return "/mypage/mydetails";
    }

    @GetMapping("/mypage/details/edit")
    public String myDetailsEditForm(Model model, Principal principal) throws Exception {
        Member member = memberService.findOneByName(principal.getName());
        UpdateMyDetailForm form = new UpdateMyDetailForm(member);

        model.addAttribute("updateMyDetailForm", form);
        if(member.getProfileImage() != null){
            model.addAttribute("fileId", member.getProfileImage().getId());
        }

        return "/mypage/updateMydetailForm";
    }

    @PostMapping("/mypage/details/edit")
    public String EditMyDetail(@Valid UpdateMyDetailForm form, BindingResult result) throws Exception {
        if(result.hasErrors()){
            return "/mypage/updateMydetailForm";
        }
        System.out.println("######"+form);

        Member member = memberService.findOneByName(form.getUserName());
        Address address = new Address();
        address.setAddress1(form.getAddress1());
        address.setAddress2(form.getAddress2());
        address.setZipcode(form.getZipcode());
        member.setAddress(address);

        try{
            Long fileId = fileService.saveFile(form.getProfileImage());
            member.setProfileImage(fileService.findOne(fileId));
        }catch(IllegalArgumentException e){
            member.setProfileImage(fileService.findOne(DefaultValue.DEFAULT_PROFILE_IMAGE_ID));
        }

        memberService.save(member);

        return "redirect:/mypage/details";
    }

    @GetMapping("/mypage/password-edit")
    public String passwordEditForm(Model model){
        model.addAttribute("updatePasswordForm", new UpdatePasswordForm());
        return "mypage/updatePasswordForm";
    }

    @PostMapping("/mypage/password-edit")
    public String passwordEdit(@Valid UpdatePasswordForm form, BindingResult result, Principal principal) throws Exception{

        if(!form.getNewPassword().equals(form.getNewPasswordCheck()))
            result.addError(new FieldError("createMemberForm",
                    "newPasswordCheck",
                    "새로운 패스워드와 패스워드확인이 일치하지 않습니다"));


        if(result.hasErrors()){
            return "mypage/updatePasswordForm";
        }

        Member member = memberService.findOneByName(principal.getName());
        member.setPassword(passwordEncoder.encode(form.getNewPassword()));
        memberService.save(member);

        return "redirect:/mypage";
    }
}
