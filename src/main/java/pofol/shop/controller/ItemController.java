package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pofol.shop.config.DefaultValue;
import pofol.shop.domain.Member;
import pofol.shop.domain.enums.Role;
import pofol.shop.dto.business.CommentDto;
import pofol.shop.dto.business.ItemDto;
import pofol.shop.dto.business.ItemSearchCondition;
import pofol.shop.dto.security.UserAdapter;
import pofol.shop.form.create.CreateCommentForm;
import pofol.shop.form.create.CreateItemForm;
import pofol.shop.domain.Item;
import pofol.shop.repository.CommentRepository;
import pofol.shop.repository.FileRepository;
import pofol.shop.repository.ItemRepository;
import pofol.shop.repository.MemberRepository;
import pofol.shop.service.FileService;
import pofol.shop.service.business.ItemService;
import pofol.shop.service.business.MemberService;
import pofol.shop.service.UtilService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static pofol.shop.config.DefaultValue.*;

/**
 * 상품과 관련된 뷰를 반환하는 Controller입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-14
 */
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final UtilService utilService;

    /**
     * 전체 상품목록 페이지를 반환합니다.
     *
     * @param condition 상품 검색조건
     * @param pageable  화면의 페이징 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-13
     */
    @GetMapping("/items") //Item 리스트
    public String list(@ModelAttribute ItemSearchCondition condition, Model model, Pageable pageable) {
        condition.removeNull();
        Page<ItemDto> results = itemRepository.searchWithPage(condition, pageable);
        utilService.pagingCommonTask(results, model); //페이지네이션 정보를 모델에 넣음

        model.addAttribute("search", condition);
        model.addAttribute("items", results.getContent());

        return "items/itemList";
    }

    /**
     * 새로운 상품을 생성하는 폼 화면을 반환합니다.
     *
     * @param principal 현재 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-20
     */
    @GetMapping("/items/new") //Item 등록 폼 화면
    public String createForm(Model model, @AuthenticationPrincipal UserAdapter principal) {
        model.addAttribute("createItemForm", new CreateItemForm());
        model.addAttribute("defaultImageId", DEFAULT_ITEM_THUMBNAIL_ID);
        return "items/createItemForm";
    }

    /**
     * 새로운 상품의 생성 요청을 처리합니다.
     *
     * @param form      상품 생성에 필요한 데이터 폼
     * @param result    폼에 입력한 데이터에 이상이 있을 경우 에러를 담는 객체
     * @param principal 현재 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-23
     */
    @PostMapping("/items") //Item 생성 요청
    public String create(@Valid CreateItemForm form, BindingResult result, @AuthenticationPrincipal UserAdapter principal) {

        //폼 입력에 문제가 있을 경우
        if (result.hasErrors()) {
            return "items/createItemForm";
        }

        Member member = memberService.findByUserName(principal.getName());

        Item item = Item.builder()
                .itemName(form.getItemName())
                .author(form.getAuthor())
                .descriptionTitle(form.getDescriptionTitle())
                .description(form.getDescription())
                .price(form.getPrice())
                .quantity(form.getQuantity())
                .member(member)
                .build();

        try { //제출한 이미지를 상품이미지로 설정
            Long fileId = fileService.saveFile(form.getThumbnail());
            item.setThumbnailFile(fileService.findById(fileId));
        } catch (IllegalArgumentException e) { //제출한 이미지가 없으면 기본이미지로 설정
            item.setThumbnailFile(fileService.findById(DefaultValue.DEFAULT_ITEM_THUMBNAIL_ID));
        } catch (IOException e) { //처리하던도중 IOException이 발생하면 기본이미지로 설정
            item.setThumbnailFile(fileService.findById(DefaultValue.DEFAULT_ITEM_THUMBNAIL_ID));
        }

        itemRepository.save(item);
        return "redirect:/items";
    }

    /**
     * 상품의 수정 폼 화면을 반환합니다.
     *
     * @param id        수정할 상품의 id
     * @param principal 현재 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-23
     */
    @GetMapping("/items/{id}/edit") //Item 수정 폼 화면
    public String editForm(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal UserAdapter principal) {

        Member member = memberService.findByUserName(principal.getName());
        Item item = itemService.findById(id);

        //로그인한 Member가 어드민계정이나 해당 아이템을 등록한 Member가 아니면 홈페이지로 되돌려 보냄
        if (member.getRole() != Role.ROLE_ADMIN && item.getMember() != member) {
            return "redirect:/";
        }

        CreateItemForm form = CreateItemForm.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .author(item.getAuthor())
                .description(item.getDescription())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .build();

        model.addAttribute("itemForm", form);

        return "items/updateItemForm";
    }

    /**
     * 상품의 수정 요청을 처리하고 뷰를 반환합니다.
     *
     * @param form      상품 생성에 필요한 폼 데이터
     * @param result    폼에 입력한 데이터에 이상이 있을 경우 에러를 담는 객체
     * @param principal 현재 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-23
     */
    @PostMapping("/items/{id}/edit") //Item 수정 요청
    public String edit(@Valid CreateItemForm form,
                       BindingResult result,
                       @PathVariable("id") Long id,
                       @AuthenticationPrincipal UserAdapter principal
    ) {

        Item item = itemService.findById(id);
        Member member = memberService.findByUserName(principal.getUsername());
        if (item.getMember() != member) { //요청한 회원이 아이템을 생성한 회원이 아니라면
            return "redirect:/";
        }

        if (result.hasErrors()) {
            return "items/updateItemForm";
        }
        itemService.edit(form.getId(), form.getItemName(), form.getPrice(), form.getQuantity());
        return "redirect:/mypage/myitems";
    }

    /**
     * 상품의 판매 상세 페이지 화면을 반환합니다.
     *
     * @param id        조회할 상품의 id
     * @param principal 현재 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-24
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-23
     */
    @GetMapping("/items/{itemId}") //Item 상세 페이지 화면
    public String item(@PathVariable("itemId") Long id, @AuthenticationPrincipal UserAdapter principal, Model model) {

        //상품에 달린 후기와 각 후기를 작성한 회원까지 페치조인
        Item item = itemService.findByIdFetchCommentsWithMember(id);
        ItemDto itemDto = new ItemDto(item);

        model.addAttribute("itemDto", itemDto);

        //로그인을 안했으면 username = Guest
        String userName = principal != null ? principal.getName() : "Guest";

        model.addAttribute("userName", userName); //바로구매, 장바구니 추가를 위한 데이터

        //상품 이미지 설정이 안되어있을 경우 기본 이미지로 설정
        if (item.getThumbnailFile() == null) {
            item.setThumbnailFile(fileService.findById(DefaultValue.DEFAULT_ITEM_THUMBNAIL_ID));
        }

        model.addAttribute("fileId", item.getThumbnailFile().getId()); //상품 이미지

        //Comment 작성 폼 데이터
        CreateCommentForm commentForm = new CreateCommentForm();
        commentForm.setItemId(id);
        model.addAttribute("commentForm", commentForm);

        //Item에 달린 Comment List 데이터
        List<CommentDto> commentDtos = item.getComments().stream().map(CommentDto::new).collect(Collectors.toList());
        model.addAttribute("comments", commentDtos);
        return "items/item";
    }
}
