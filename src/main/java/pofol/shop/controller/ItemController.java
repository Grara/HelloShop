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
import pofol.shop.dto.CommentDto;
import pofol.shop.dto.ItemDto;
import pofol.shop.dto.ItemSearchCondition;
import pofol.shop.dto.UserAdapter;
import pofol.shop.form.create.CreateCommentForm;
import pofol.shop.form.create.CreateItemForm;
import pofol.shop.domain.Comment;
import pofol.shop.domain.Item;
import pofol.shop.repository.CommentRepository;
import pofol.shop.repository.FileRepository;
import pofol.shop.repository.ItemRepository;
import pofol.shop.repository.MemberRepository;
import pofol.shop.service.FileService;
import pofol.shop.service.ItemService;
import pofol.shop.service.MemberService;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/items") //Item 리스트
    public String list(@ModelAttribute ItemSearchCondition condition, Model model, Pageable pageable){
        Page<ItemDto> results = itemRepository.searchWithPage(condition, pageable);

        int pageStart = results.getNumber() / 10 * 10 + 1;

        int pageEnd = Math.min(pageStart + 9, results.getTotalPages());
        if(pageEnd <= 0) pageEnd = 1;

        model.addAttribute("totalPage", results.getTotalPages());
        model.addAttribute("pageStart", pageStart);
        model.addAttribute("pageEnd", pageEnd);
        model.addAttribute("curNumber", results.getNumber() + 1); //현재 페이지 번호
        model.addAttribute("search", condition);
        model.addAttribute("items", results.getContent());

        return "items/itemList";
    }

    @GetMapping("/items/new") //Item 등록 폼 화면
    public String createForm(Model model){
        model.addAttribute("createItemForm", new CreateItemForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new") //Item 등록 요청
    public String create(@Valid CreateItemForm form, BindingResult result, Principal principal){

        if (result.hasErrors()) {
            return "items/createItemForm";
        }

        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAuthor(form.getAuthor());
        item.setDescriptionTitle(form.getDescriptionTitle());
        item.setDescription(form.getDescription());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());
        item.setMember(memberRepository.findByUserName(principal.getName()).orElseThrow());

        try{ //제출한 이미지를 상품이미지로 설정
            Long fileId = fileService.saveFile(form.getThumbnail());
            item.setThumbnailFile(fileRepository.findById(fileId).orElseThrow());
        }catch(IllegalArgumentException e){ //제출한 이미지가 없으면 기본이미지로 설정
            item.setThumbnailFile(fileRepository.findById(DefaultValue.DEFAULT_ITEM_THUMBNAIL_ID).orElseThrow());
        }catch(IOException e){ //처리하던도중 IOException이 발생하면 기본이미지로 설정
            item.setThumbnailFile(fileRepository.findById(DefaultValue.DEFAULT_ITEM_THUMBNAIL_ID).orElseThrow());
        }

        itemRepository.save(item);
        return "redirect:/items";
    }

    @GetMapping("/items/edit") //Item 수정 폼 화면
    public String editForm(@RequestParam Long id, Model model, Principal principal){

        Member member = memberRepository.findByUserName(principal.getName()).orElseThrow();
        Item item = itemRepository.findById(id).orElseThrow();

        //로그인한 Member가 어드민계정이나 해당 아이템을 등록한 Member가 아니면 홈페이지로 되돌려 보냄
        if (member.getRole() != Role.ROLE_ADMIN && item.getMember() != member) {
            return "redirect:/";
        }

        CreateItemForm form = new CreateItemForm();
        form.setId(item.getId());
        form.setItemName(item.getItemName());
        form.setAuthor(item.getAuthor());
        form.setDescription(item.getDescription());
        form.setPrice(item.getPrice());
        form.setQuantity(item.getQuantity());
        model.addAttribute("itemForm", form);

        return "/items/updateItemForm";
    }

    @PostMapping("/items/edit") //Item 수정 요청
    public String edit(@Valid CreateItemForm form, BindingResult result){

        if (result.hasErrors()) {
            return "/items/updateItemForm";
        }
        itemService.edit(form.getId(), form.getItemName(), form.getPrice(), form.getQuantity());
        return "redirect:/mypage/myitems";
    }

    @GetMapping("/items/{itemId}") //Item 상세 페이지 화면
    public String item(@PathVariable("itemId") Long id, Model model, @AuthenticationPrincipal UserAdapter principal) {

        Item item = itemRepository.findById(id).orElseThrow();
        ItemDto itemDto = new ItemDto(item);

        model.addAttribute("itemDto", itemDto);

        //로그인을 안했으면 username = Guest
        String userName = principal != null ? principal.getName() : "Guest";

        model.addAttribute("userName", userName); //바로구매, 장바구니 추가를 위한 데이터

        //상품 이미지 설정이 안되어있을 경우 기본 이미지로 설정
        if (item.getThumbnailFile() == null) {
            item.setThumbnailFile(fileRepository.findById(DefaultValue.DEFAULT_ITEM_THUMBNAIL_ID).orElseThrow());
        }

        model.addAttribute("fileId", item.getThumbnailFile().getId()); //상품 이미지

        //Comment 작성 폼 데이터
        CreateCommentForm commentForm = new CreateCommentForm();
        commentForm.setItemId(id);
        model.addAttribute("commentForm", commentForm);

        //Item에 달린 Comment List 데이터
        List<CommentDto> commentDtos = item.getComments().stream().map(CommentDto::new).collect(Collectors.toList());
        model.addAttribute("comments", commentDtos);
        return "/items/item";
    }


}
