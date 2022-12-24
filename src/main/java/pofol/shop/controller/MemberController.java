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
import org.springframework.web.bind.annotation.RequestParam;
import pofol.shop.dto.business.MemberDto;
import pofol.shop.dto.business.MemberSearchCondition;
import pofol.shop.dto.security.UserAdapter;
import pofol.shop.form.LoginForm;
import pofol.shop.form.create.CreateMemberForm;
import pofol.shop.domain.Member;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.embedded.PersonalInfo;
import pofol.shop.domain.enums.Role;
import pofol.shop.form.create.CreateOAuth2MemberForm;
import pofol.shop.repository.MemberRepository;
import pofol.shop.service.business.MemberService;
import pofol.shop.service.UtilService;

import javax.validation.Valid;
import java.util.*;

/**
 * 회원과 관련된 뷰를 반환하는 Controller입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-21
 */
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final UtilService utilService;

    /**
     * 로그인 화면을 반환합니다.
     *
     * @param error 로그인 과정에 에러가 있었는지 여부
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-21
     */
    @GetMapping("/login-form") //로그인화면
    public String loginForm(@RequestParam(value = "error", required = false) boolean error, Model model) {
        SecurityContextHolder.getContext().setAuthentication(null);
        LoginForm form = new LoginForm();
        model.addAttribute("loginForm", form);
        model.addAttribute("hasError", error);
        return "loginForm";
    }

    /**
     * Oauth2로그인 성공 시 회원가입 폼 화면을 반환합니다. <br/>
     * 이미 회원으가입을 한 상태라면 홈화면으로 이동합니다.
     *
     * @param principal 현재 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-12
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-19
     */
    @GetMapping("/members/new-oauth2") //OAuth2로그인 성공 시
    public String oauth2LoginSuccess(@AuthenticationPrincipal UserAdapter principal, Model model) {
        Optional<Member> member = memberRepository.findByEmail(principal.getAttribute("email"));

        if (member.isPresent()) { //이미 존재하는 회원이면 홈페이지로
            return "redirect:/";
        }

        //Oauth2용 회원가입 폼 생성
        CreateOAuth2MemberForm form = CreateOAuth2MemberForm.builder()
                .email(principal.getAttribute("email"))
                .realName(principal.getAttribute("name"))
                .build();

        model.addAttribute("createOAuth2MemberForm", form);

        return "members/createMemberForm-OAuth2";
    }

    /**
     * 아직 회원가입을 안한 Oauth2유저의 회원가입 요청을 처리하고 뷰를 반환합니다.
     * 회원가입 후 인가 권한을 갱신하기 위해 세션정보를 제거합니다.
     *
     * @param form      회원가입에 필요한 데이터 폼
     * @param result    폼에 입력한 데이터에 이상이 있을 경우 에러를 담는 객체
     * @param principal 현재 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-12
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-19
     */
    @PostMapping("/members/new-oauth2") //OAuth2 회원가입 요청
    public String createOauth2Member(@Valid CreateOAuth2MemberForm form, BindingResult result, @AuthenticationPrincipal UserAdapter principal) {

        //값 입력에 문제가 있으면 다시 수정하도록함
        if (result.hasErrors()) {
            return "members/createMemberForm-OAuth2";
        }
        if (!principal.getAttribute("email").equals(form.getEmail())) {
            return "redirect:/";
        }

        SecurityContextHolder.getContext().setAuthentication(null); //로그인 세션 해제

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

        return "redirect:/login-form";
    }


    /**
     * 아직 회원가입을 안한 Oauth2유저의 회원가입 요청을 처리하고 뷰를 반환합니다.
     * 회원가입 후 인가 권한을 갱신하기 위해 세션정보를 제거합니다.
     *
     * @param condition 회원 검색 조건
     * @param pageable  화면 페이징 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-01
     */
    @GetMapping("/members") //전체 Member 목록
    public String list(@ModelAttribute MemberSearchCondition condition, Model model, Pageable pageable) {
        //페이징 정보와 회원DTO목록을 받아옴
        Page<MemberDto> results = memberRepository.searchWithPage(condition, pageable);
        utilService.pagingCommonTask(results, model); //페이징 관련 정보를 모델 데이터에 추가

        model.addAttribute("search", condition);
        model.addAttribute("members", results.getContent());

        return "members/memberList";
    }

    /**
     * 일반회원의 회원가입 폼 화면을 반환합니다.
     *
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-20
     */
    @GetMapping("/members/new") //Member 가입 폼 화면
    public String createForm(Model model) {
        model.addAttribute("createMemberForm", new CreateMemberForm());
        return "members/createMemberForm";
    }

    /**
     * 일반회원의 회원가입 요청을 처리하고 뷰를 반환합니다.
     *
     * @param form 회원가입처리에 필요한 데이터 폼
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-13
     */
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
