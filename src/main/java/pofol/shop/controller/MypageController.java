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
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;
import pofol.shop.domain.Order;
import pofol.shop.domain.embedded.Address;
import pofol.shop.dto.OrderSearchCondition;
import pofol.shop.form.update.UpdateImageForm;
import pofol.shop.form.update.UpdateMyDetailForm;
import pofol.shop.form.update.UpdatePasswordForm;
import pofol.shop.repository.FileRepository;
import pofol.shop.repository.ItemRepository;
import pofol.shop.repository.MemberRepository;
import pofol.shop.repository.OrderRepository;
import pofol.shop.service.FileService;
import pofol.shop.service.ItemService;
import pofol.shop.service.MemberService;
import pofol.shop.service.OrderService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

import static pofol.shop.config.DefaultValue.DEFAULT_PROFILE_IMAGE_ID;

@Controller
@RequiredArgsConstructor
public class MypageController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @GetMapping("/mypage") //마이페이지 홈
    public String mypageHome(Model model, Principal principal){

        Member member = memberRepository.findByUserName(principal.getName()).orElseThrow();

        if (member.getProfileImage() != null) { //프로필 이미지 파일 id 추가
            model.addAttribute("fileId", member.getProfileImage().getId());
        }

        model.addAttribute("userName", member.getUserName());

        return "/mypage/myhome";

    }

    @GetMapping("/mypage/details") //개인정보 화면
    public String mypageDetails(Model model, Principal principal){

        Member member = memberRepository.findByUserName(principal.getName()).orElseThrow();
        UpdateMyDetailForm form = new UpdateMyDetailForm(member);
        form.setProfileId(member.getProfileImage().getId());

        model.addAttribute("form", form);

        return "/mypage/mydetails";
    }

    @GetMapping("/mypage/details/edit") //개인정보 수정 폼 화면
    public String myDetailsEditForm(Model model, Principal principal){

        Member member = memberRepository.findByUserName(principal.getName()).orElseThrow();
        UpdateMyDetailForm form = new UpdateMyDetailForm(member);
        form.setProfileId(member.getProfileImage().getId());

        model.addAttribute("updateMyDetailForm", form);

        return "/mypage/updateMydetailForm";
    }

    @PostMapping("/mypage/details/edit") //개인정보 수정 요청
    public String EditMyDetail(@Valid UpdateMyDetailForm form, BindingResult result){

        if(result.hasErrors()){
            return "/mypage/updateMydetailForm";
        }

        Member member = memberRepository.findByUserName(form.getUserName()).orElseThrow();
        Address address = new Address();
        address.setAddress1(form.getAddress1());
        address.setAddress2(form.getAddress2());
        address.setZipcode(form.getZipcode());
        member.setAddress(address);

        memberRepository.save(member);

        return "redirect:/mypage/details";
    }

    @GetMapping("/mypage/password-edit") //비밀번호 변경 폼 화면
    public String passwordEditForm(Model model, Principal principal){

        model.addAttribute("updatePasswordForm", new UpdatePasswordForm());
        return "mypage/updatePasswordForm";
    }

    @PostMapping("/mypage/password-edit") //비밀번호 변경 요청
    public String passwordEdit(@Valid UpdatePasswordForm form, BindingResult result, Principal principal){

        Member member = memberRepository.findByUserName(principal.getName()).orElseThrow();

        //현재 비밀번호입력이 틀렸을 경우 오류 추가
        if(!passwordEncoder.matches(form.getCurPassword(), member.getPassword())){
            result.addError(new FieldError("createMemberForm",
                    "curPassword",
                    "기존 비밀번호를 잘못 입력했습니다"));
        }

        //새로운 비밀번호와 비밀번호 확인 입력이 다를 경우 오류 추가
        if(!form.getNewPassword().equals(form.getNewPasswordCheck()))
            result.addError(new FieldError("createMemberForm",
                    "newPasswordCheck",
                    "새로운 비밀번호와 비밀번호 확인이 일치하지 않습니다"));

        //입력에 오류가 있을 경우 다시 변경 폼 화면으로
        if(result.hasErrors()){
            return "mypage/updatePasswordForm";
        }

        member.setPassword(passwordEncoder.encode(form.getNewPassword()));
        memberRepository.save(member);

        return "redirect:/mypage";
    }

    @GetMapping("/mypage/details/profile/update") //프로필 변경 폼 화면(팝업창)
    public String updateProfileImageForm(Principal principal){
        return "popup/updateProfileForm";
    }

    @PostMapping("/mypage/details/profile/update") //프로필 변경 요청
    public String updateProfile(UpdateImageForm form, Principal principal, Model model){

        Member member = memberRepository.findByUserName(principal.getName()).orElseThrow();
        long returnId = 0l; //모델에 전달할 FileEntity id값

        try {
            //멤버의 프로필을 제출한 이미지 파일로 설정
            Long fileId = fileService.saveFile(form.getUploadImg());
            member.setProfileImage(fileRepository.findById(fileId).orElseThrow());
            memberRepository.save(member);
            returnId = fileId;

        }catch (IllegalArgumentException e){
            //이미지 파일이 없을 경우 프로필을 기본 이미지로 설정
            member.setProfileImage(fileRepository.findById(DEFAULT_PROFILE_IMAGE_ID).orElseThrow());
            memberRepository.save(member);
            returnId = DEFAULT_PROFILE_IMAGE_ID;

        }catch (IOException e){
            //IO관련 문제가 발생할 경우 기존 프로필 유지
            returnId = member.getProfileImage().getId();
        }

        //팝업창을 닫힐 때 부모창의 프로필 이미지를 바꾸기위한 url
        model.addAttribute("imgUrl", "/images/" + returnId);
        return "popup/finishUpdateProfile";
    }

    @GetMapping("/mypage/myitems") //내가 판매하는 아이템 리스트 화면
    public String myItemList(Model model, Principal principal){

        Member member = memberRepository.findByUserName(principal.getName()).orElseThrow();
        List<Item> items = itemRepository.findListByMember(member);
        model.addAttribute("items", items);
        return "mypage/myitems";
    }

    @GetMapping("/mypage/myorders") //내 구매내역
    public String myorders(@ModelAttribute OrderSearchCondition condition, Model model, Principal principal){

        Member member = memberRepository.findByUserName(principal.getName()).orElseThrow();
        condition.setMember(member);
        List<Order> orders = orderRepository.search(condition); //검색 조건으로 주문 목록을 찾음
        model.addAttribute("orderSearch", condition);
        model.addAttribute("orders", orders);
        return "/mypage/myorders";
    }
}