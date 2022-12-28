package pofol.shop.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;
import pofol.shop.domain.embedded.Address;
import pofol.shop.dto.business.OrderDto;
import pofol.shop.dto.business.OrderSearchCondition;
import pofol.shop.dto.security.UserAdapter;
import pofol.shop.form.update.UpdateImageForm;
import pofol.shop.form.update.UpdateMyDetailForm;
import pofol.shop.form.update.UpdatePasswordForm;
import pofol.shop.repository.FileRepository;
import pofol.shop.repository.ItemRepository;
import pofol.shop.repository.MemberRepository;
import pofol.shop.repository.OrderRepository;
import pofol.shop.service.*;
import pofol.shop.service.FileService;
import pofol.shop.service.business.MemberService;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static pofol.shop.config.DefaultValue.*;

/**
 * 마이페이지와 관련된 뷰를 반환하는 Controller입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-30
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-28
 */
@Controller
@RequiredArgsConstructor
public class MypageController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final UtilService utilService;

    /**
     * 개인정보 조회 화면을 반환합니다.
     *
     * @param principal 현재 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-14
     */
    @GetMapping("/mypage/details") //개인정보 화면
    public String mypageDetails(Model model, @AuthenticationPrincipal UserAdapter principal) {

        Member member = memberService.findByUserName(principal.getName());
        UpdateMyDetailForm form = new UpdateMyDetailForm(member);
        form.setProfileId(member.getProfileImage().getId());

        model.addAttribute("form", form);
        return "mypage/mydetails";
    }

    /**
     * 개인정보 수정 폼 화면을 반환합니다.
     *
     * @param principal 현재 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-14
     */
    @GetMapping("/mypage/details/edit") //개인정보 수정 폼 화면
    public String myDetailsEditForm(Model model, @AuthenticationPrincipal UserAdapter principal) {

        Member member = memberService.findByUserName(principal.getName());
        UpdateMyDetailForm form = new UpdateMyDetailForm(member);
        form.setProfileId(member.getProfileImage().getId());

        model.addAttribute("updateMyDetailForm", form);

        return "mypage/updateMydetailForm";
    }

    /**
     * 개인정보 수정 요청을 처리하고 뷰를 반환합니다.
     *
     * @param form      개인정보 수정에 필요한 데이터 폼
     * @param principal 현재 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-28
     */
    @PatchMapping("/mypage/details")
    public String EditMyDetail(@Valid UpdateMyDetailForm form, BindingResult result, @AuthenticationPrincipal UserAdapter principal) {

        //폼 입력에 문제가 있을 경우 수정하도록 함
        if (result.hasErrors()) {
            return "mypage/updateMydetailForm";
        }

        Member member = memberService.findByUserName(form.getUserName());
        Address address = new Address(form.getAddress1(), form.getAddress2(), form.getZipcode());
        member.setAddress(address);
        memberRepository.save(member);

        return "redirect:/mypage/details";
    }

    /**
     * 비밀번호 변경 폼 화면을 반환합니다.
     *
     * @param principal 현재 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-30
     */
    @GetMapping("/mypage/password-edit") //비밀번호 변경 폼 화면
    public String updatePasswordForm(Model model, @AuthenticationPrincipal UserAdapter principal) {

        model.addAttribute("updatePasswordForm", new UpdatePasswordForm());
        return "mypage/updatePasswordForm";
    }

    /**
     * 비밀번호 변경 요청을 처리하고 뷰를 반환합니다.
     *
     * @param principal 현재 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-30
     */
    @PostMapping("/mypage/password-edit") //비밀번호 변경 요청
    public String updatePassword(@Valid UpdatePasswordForm form, BindingResult result, @AuthenticationPrincipal UserAdapter principal) {

        Member member = memberService.findByUserName(principal.getName());

        //현재 비밀번호입력이 틀렸을 경우 오류 추가
        if (!passwordEncoder.matches(form.getCurPassword(), member.getPassword())) {
            result.addError(new FieldError("createMemberForm",
                    "curPassword",
                    "기존 비밀번호를 잘못 입력했습니다"));
        }

        //새로운 비밀번호와 비밀번호 확인 입력이 다를 경우 오류 추가
        if (!form.getNewPassword().equals(form.getNewPasswordCheck()))
            result.addError(new FieldError("createMemberForm",
                    "newPasswordCheck",
                    "새로운 비밀번호와 비밀번호 확인이 일치하지 않습니다"));

        //입력에 오류가 있을 경우 다시 변경 폼 화면으로
        if (result.hasErrors()) {
            return "mypage/updatePasswordForm";
        }

        member.setPassword(passwordEncoder.encode(form.getNewPassword()));
        memberRepository.save(member);

        SecurityContextHolder.getContext().setAuthentication(null);

        return "redirect:/login-form";
    }

    /**
     * 프로필 변경 팝업 창 화면을 반환합니다.
     *
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-28
     */
    @GetMapping("/mypage/profile-img-update") //프로필 변경 폼 화면(팝업창)
    public String updateProfileImageForm(Model model) {

        //기본이미지의 FileEntity id값
        model.addAttribute("defaultImageId", DEFAULT_PROFILE_IMAGE_ID);
        return "popup/updateProfileForm";
    }

    /**
     * 프로필 변경 요청을 처리하고 뷰를 반환합니다.
     *
     * @param principal 현재 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-28
     */
    @PostMapping("/mypage/profile-img-update") //프로필 변경 요청
    public String updateProfile(UpdateImageForm form, Model model, @AuthenticationPrincipal UserAdapter principal) {

        Member member = memberService.findByUserName(principal.getName());
        long returnId = 0l; //모델에 전달할 FileEntity id값

        try {
            //멤버의 프로필을 제출한 이미지 파일로 설정
            Long fileId = fileService.saveFile(form.getUploadImg());
            member.setProfileImage(fileService.findById(fileId));
            memberRepository.save(member);
            returnId = fileId;

        } catch (IllegalArgumentException e) {
            //이미지 파일이 없을 경우 프로필을 기본 이미지로 설정
            member.setProfileImage(fileService.findById(DEFAULT_PROFILE_IMAGE_ID));
            memberRepository.save(member);
            returnId = DEFAULT_PROFILE_IMAGE_ID;

        } catch (IOException e) {
            //IO관련 문제가 발생할 경우 기존 프로필 유지
            returnId = member.getProfileImage().getId();
        }

        //팝업창을 닫힐 때 부모창의 프로필 이미지를 바꾸기위한 url
        model.addAttribute("imgUrl", "/images/" + returnId);
        return "popup/finishUpdateProfile";
    }

    /**
     * 내가 생성한 상품의 목록의 리스트 화면을 반환합니다.
     *
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-30
     */
    @GetMapping("/mypage/myitems") //내가 판매하는 상품 리스트 화면
    public String myItemList(Model model, @AuthenticationPrincipal UserAdapter principal) {

        Member member = memberService.findByUserName(principal.getName());
        List<Item> items = itemRepository.findListByMember(member);
        model.addAttribute("items", items);
        return "mypage/myitems";
    }

    /**
     * 내 구매 내역 리스트의 화면을 반환합니다.
     *
     * @param condition 주문 검색 조건
     * @param principal 현재 로그인 세션 정보
     * @param pageable  페이징 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-14
     */
    @GetMapping("/mypage/myorders") //내 구매내역
    public String myorders(@ModelAttribute OrderSearchCondition condition,
                           Model model,
                           Pageable pageable,
                           @AuthenticationPrincipal UserAdapter principal) {

        Member member = memberService.findByUserName(principal.getName());
        condition.setUserName(member.getUserName());

        //시작날짜로 입력 받은 String값을 해당날짜의 00시00분 LocalDateTime으로 변환
        if (StringUtils.hasText(condition.getStartDateInput())) {
            LocalDate temp = LocalDate.parse(condition.getStartDateInput());
            condition.setStartDate(temp.atStartOfDay());
        }

        //종료날짜로 입력 받은 String값을 해당날짜의 23시59분 LocalDateTime으로 변환
        if (StringUtils.hasText(condition.getEndDateInput())) {
            LocalDate temp = LocalDate.parse(condition.getEndDateInput());
            condition.setEndDate(temp.atTime(23, 59));
        }

        //페이징 관련 데이터들과 검색 조건에 해당하는 주문들의 dto리스트가 담긴 객체
        Page<OrderDto> results = orderRepository.searchWithPage(condition, pageable);
        utilService.pagingCommonTask(results, model); //모델에 페이징 관련 데이터 추가
        model.addAttribute("search", condition);
        model.addAttribute("orders", results.getContent());

        return "mypage/myorders";
    }
}
