package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pofol.shop.dto.MemberDto;
import pofol.shop.dto.MemberSearchCondition;
import pofol.shop.dto.UserAdapter;
import pofol.shop.form.create.CreateMemberForm;
import pofol.shop.domain.Member;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.embedded.PersonalInfo;
import pofol.shop.domain.enums.Role;
import pofol.shop.form.create.CreateOAuth2MemberForm;
import pofol.shop.repository.MemberRepository;
import pofol.shop.service.MemberService;
import pofol.shop.service.UtilService;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final UtilService utilService;


    @GetMapping("/members/new-oauth2") //OAuth2로그인 성공 시
    public String oauth2LoginSuccess(@AuthenticationPrincipal UserAdapter principal, Model model) {
        Optional<Member> member = memberRepository.findByEmail(principal.getAttribute("email"));

        if (member.isPresent()) { //이미 존재하는 회원이면 홈페이지로
            return "redirect:/";
        }

        CreateOAuth2MemberForm form = CreateOAuth2MemberForm.builder()
                .email(principal.getAttribute("email"))
                .realName(principal.getAttribute("name"))
                .build();

        model.addAttribute("createOAuth2MemberForm", form);

        return "/members/createMemberForm-Oauth2";

    }

    @PostMapping("/members/new-oauth2") //OAuth2 회원가입 요청
    public String createOauth2(@Valid CreateOAuth2MemberForm form, BindingResult result, @AuthenticationPrincipal UserAdapter principal) {

        //값 입력에 문제가 있으면 다시 수정하도록함
        if (result.hasErrors()) {
            return "members/createMemberForm-Oauth2";
        }

        if(!principal.getAttribute("email").equals(form.getEmail())){
            return "redirect:/";
        }

        try {
            Address address = new Address(form.getAddress1(), form.getAddress2(), form.getZipcode());
            PersonalInfo personalInfo = new PersonalInfo(form.getRealName(), form.getAge(), form.getSex());
            Member member = Member.builder()
                    .userName(form.getUserName())
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .email(form.getEmail())
                    .address(address)
                    .personalInfo(personalInfo)
                    .role(Role.ROLE_USER)
                    .build();

            memberService.signUp(member);
            SecurityContextHolder.getContext().setAuthentication(null);

            return "redirect:/login-form";
        } catch (Exception e) {
            throw e;
        }
    }


    @GetMapping("/members") //전체 Member 목록
    public String list(@ModelAttribute MemberSearchCondition condition, Model model, Pageable pageable) {
        //페이징 정보와 회원DTO목록을 받아옴
        Page<MemberDto> results = memberRepository.searchWithPage(condition, pageable);
        utilService.pagingCommonTask(results, model);

        model.addAttribute("search", condition);
        model.addAttribute("members", results.getContent());

        return "members/memberList";
    }

    @GetMapping("/members/new") //Member 가입 폼 화면
    public String createForm(Model model) {
        model.addAttribute("createMemberForm", new CreateMemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new") //Member 가입 요청
    public String create(@Valid CreateMemberForm form, BindingResult result) {

        if (!form.getPassword().equals(form.getPasswordCheck()))
            result.addError(new FieldError("createMemberForm",
                    "passwordCheck",
                    "패스워드와 패스워드확인이 일치하지 않습니다"));

        //값 입력에 문제가 있으면 다시 수정하도록함
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        try {
            Address address = new Address(form.getAddress1(), form.getAddress2(), form.getZipcode());
            PersonalInfo personalInfo = new PersonalInfo(form.getRealName(), form.getAge(), form.getSex());

            Member member = Member.builder()
                    .userName(form.getUserName())
                    .password(passwordEncoder.encode(form.getPassword()))
                    .address(address)
                    .personalInfo(personalInfo)
                    .role(Role.ROLE_USER)
                    .build();

            memberService.signUp(member);
            return "redirect:/";

        } catch (Exception e) {
            return "errors/unknownError";
        }
    }
}
