package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
import pofol.shop.repository.MemberRepository;
import pofol.shop.service.MemberService;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/members/new-oauth2") //OAuth2로그인 성공 시
    public String oauth2LoginSuccess(Authentication authentication, Model model) {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        Optional<Member> member = memberRepository.findByEmail(user.getAttribute("email"));
        if (member.isPresent()) { //이미 존재하는 회원이면 홈페이지로
            return "redirect:/";
        }
        CreateMemberForm form = CreateMemberForm.builder()
                .email(user.getAttribute("email"))
                .realName(user.getAttribute("name"))
                .build();

        model.addAttribute("createMemberForm", form);

        return "/members/createMemberForm-Oauth2";

    }

    @PostMapping("/members/new-oauth2") //OAuth2 회원가입 요청
    public String createOauth2(CreateMemberForm form, BindingResult result, @AuthenticationPrincipal UserAdapter userDetail) {

        //값 입력에 문제가 있으면 다시 수정하도록함
        if (result.hasErrors()) {
            return "members/createMemberForm";
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
            MemberDto dto = new MemberDto(member);
            userDetail.setMember(dto);

            SecurityContextHolder.getContext().setAuthentication(null);


            return "redirect:/login";
        } catch (Exception e) {
            throw e;
        }
    }


    @GetMapping("/members") //전체 Member 목록
    public String list(@ModelAttribute MemberSearchCondition condition, Model model, Pageable pageable) {
        //페이징 정보와 회원DTO목록을 받아옴
        Page<MemberDto> results = memberRepository.searchWithPage(condition, pageable);

        //페이지 이동 버튼의 시작 페이지 번호
        //1~10, 11~20 ··· 이런식으로 UI에 보여짐
        int pageStart = results.getNumber() / 10 * 10 + 1;

        //페이지 이동 버튼 끝 페이지 번호, 전체 페이지 중 마지막 페이지까지만
        int pageEnd = Math.min(pageStart + 9, results.getTotalPages());
        if (pageEnd <= 0) pageEnd = 1;

        model.addAttribute("totalPage", results.getTotalPages());
        model.addAttribute("pageStart", pageStart);
        model.addAttribute("pageEnd", pageEnd);
        model.addAttribute("curNumber", results.getNumber() + 1); //현재 페이지 번호
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

        //비밀번호와 비밀번호 확인이 일치하는지 체크
        if (!form.getPassword().equals(form.getPasswordCheck()))
            result.addError(new FieldError("createMemberForm",
                    "passwordCheck",
                    "패스워드와 패스워드확인이 일치하지 않습니다"));

        //값 입력에 문제가 있으면 다시 수정하도록함
        if (result.hasErrors()) {
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
        } catch (Exception e) {
            return "errors/unknownError";
        }
    }
}
