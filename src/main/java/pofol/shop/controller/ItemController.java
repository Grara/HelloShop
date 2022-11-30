package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pofol.shop.config.DefaultValue;
import pofol.shop.domain.Member;
import pofol.shop.domain.enums.Role;
import pofol.shop.form.create.CreateCommentForm;
import pofol.shop.form.create.CreateItemForm;
import pofol.shop.domain.Comment;
import pofol.shop.domain.Item;
import pofol.shop.repository.CommentRepository;
import pofol.shop.service.FileService;
import pofol.shop.service.ItemService;
import pofol.shop.service.MemberService;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CommentRepository commentRepository;
    private final FileService fileService;
    private final MemberService memberService;

    @GetMapping("/items") //Item 리스트
    public String list(Model model){
        List<Item> items = itemService.findList();
        model.addAttribute("items", items);
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
        item.setDescription(form.getDescription());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());
        item.setMember(memberService.findOneByName(principal.getName()));

        try{ //제출한 이미지를 상품이미지로 설정
            Long fileId = fileService.saveFile(form.getThumbnail());
            item.setThumbnailFile(fileService.findOne(fileId));
        }catch(IllegalArgumentException e){ //제출한 이미지가 없으면 기본이미지로 설정
            item.setThumbnailFile(fileService.findOne(DefaultValue.DEFAULT_ITEM_THUMBNAIL_ID));
        }catch(IOException e){ //처리하던도중 IOException이 발생하면 기본이미지로 설정
            item.setThumbnailFile(fileService.findOne(DefaultValue.DEFAULT_ITEM_THUMBNAIL_ID));
        }

        itemService.save(item);
        return "redirect:/items";
    }

    @GetMapping("/items/edit") //Item 수정 폼 화면
    public String editForm(@RequestParam Long id, Model model, Principal principal){

        Member member = memberService.findOneByName(principal.getName());
        Item item = itemService.findOne(id);

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
    public String item(@PathVariable("itemId") Long id, Model model, Principal principal) {

        Item item = itemService.findOne(id);
        CreateItemForm itemForm = new CreateItemForm();

        //아이템 정보 데이터
        itemForm.setId(item.getId());
        itemForm.setItemName(item.getItemName());
        itemForm.setAuthor(item.getAuthor());
        itemForm.setDescription(item.getDescription());
        itemForm.setPrice(item.getPrice());
        itemForm.setQuantity(item.getQuantity());
        model.addAttribute("itemForm", itemForm);

        //로그인을 안했으면 username = Guest
        String userName = principal != null ? principal.getName() : "Guest";

        model.addAttribute("userName", userName); //바로구매, 장바구니 추가를 위한 데이터

        //상품 이미지 설정이 안되어있을 경우 기본 이미지로 설정
        if (item.getThumbnailFile() == null) {
            item.setThumbnailFile(fileService.findOne(DefaultValue.DEFAULT_ITEM_THUMBNAIL_ID));
        }

        model.addAttribute("fileId", item.getThumbnailFile().getId()); //상품 이미지

        //Comment 작성 폼 데이터
        CreateCommentForm commentForm = new CreateCommentForm();
        commentForm.setItemId(id);
        commentForm.setCreatedUserName(userName);
        model.addAttribute("commentForm", commentForm);

        //Item에 달린 Comment List 데이터
        model.addAttribute("comments", item.getComments());
        return "/items/item";
    }

    @PostMapping("/items/comments/new") //Item 상세페이지 Comment 생성 요청
    public String createComment(@Valid CreateCommentForm form) {

        Comment comment = new Comment();
        Item item = itemService.findOne(form.getItemId());
        comment.setItem(item);
        comment.setCreatedUserName(form.getCreatedUserName());
        comment.setContent(form.getContent());
        commentRepository.save(comment);
        return "redirect:/items/" + form.getItemId();
    }
}
