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
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CommentRepository commentRepository;
    private final FileService fileService;
    private final MemberService memberService;

    @GetMapping("/items")
    //아이템 리스트
    public String list(Model model) throws Exception {
        List<Item> items = itemService.findList();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/mypage/myitems")
    //아이템 리스트
    public String myItemList(Model model, Principal principal) throws Exception {
        Member member = memberService.findOneByName(principal.getName());
        List<Item> items = itemService.findListByMember(member);
        model.addAttribute("items", items);
        return "mypage/myitems";
    }

    @GetMapping("/items/new")
    //아이템 등록폼 화면
    public String createForm(Model model) throws Exception {
        model.addAttribute("createItemForm", new CreateItemForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    //아이템 등록 실행
    public String create(@Valid CreateItemForm form, BindingResult result, Principal principal) throws Exception {
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

        try{
            Long fileId = fileService.saveFile(form.getThumbnail());
            item.setThumbnailFile(fileService.findOne(fileId));
        }catch(IllegalArgumentException e){
            item.setThumbnailFile(fileService.findOne(DefaultValue.DEFAULT_ITEM_THUMBNAIL_ID));
        }

        itemService.save(item);
        return "redirect:/items";
    }

    @GetMapping("/items/edit")
    //아이템 수정 폼 화면
    public String editForm(@RequestParam Long id, Model model, Principal principal) throws Exception {
        Member member = memberService.findOneByName(principal.getName());
        Item item = itemService.findOne(id);
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

    @PostMapping("/items/edit")
    //아이템 수정 실행
    public String edit(@Valid CreateItemForm form, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            return "/items/updateItemForm";
        }
        itemService.edit(form.getId(), form.getItemName(), form.getPrice(), form.getQuantity());
        return "redirect:/mypage/myitems";
    }

    @GetMapping("/items/{itemId}")
    public String item(@PathVariable("itemId") Long id, Model model, Principal principal) throws Exception {
        Item item = itemService.findOne(id);
        CreateItemForm itemForm = new CreateItemForm();

        itemForm.setId(item.getId());
        itemForm.setItemName(item.getItemName());
        itemForm.setAuthor(item.getAuthor());
        itemForm.setDescription(item.getDescription());
        itemForm.setPrice(item.getPrice());
        itemForm.setQuantity(item.getQuantity());
        model.addAttribute("itemForm", itemForm);

        String userName = principal != null ? principal.getName() : "Guest";
        model.addAttribute("userName", userName);

        if (item.getThumbnailFile() == null) {
            item.setThumbnailFile(fileService.findOne(DefaultValue.DEFAULT_ITEM_THUMBNAIL_ID));
        }

        model.addAttribute("fileId", item.getThumbnailFile().getId());


        CreateCommentForm commentForm = new CreateCommentForm();
        commentForm.setItemId(id);
        commentForm.setCreatedUserName(userName);
        model.addAttribute("commentForm", commentForm);
        model.addAttribute("comments", item.getComments());
        return "/items/item";
    }

    @PostMapping("/items/createComment")
    public String createComment(@Valid CreateCommentForm form) throws Exception {
        Comment comment = new Comment();
        Item item = itemService.findOne(form.getItemId());
        comment.setItem(item);
        comment.setCreatedUserName(form.getCreatedUserName());
        comment.setContent(form.getContent());
        commentRepository.save(comment);
        return "redirect:/items/" + form.getItemId();
    }
}
