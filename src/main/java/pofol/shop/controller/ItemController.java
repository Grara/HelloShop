package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pofol.shop.formAndDto.CommentForm;
import pofol.shop.formAndDto.ItemForm;
import pofol.shop.domain.Comment;
import pofol.shop.domain.Item;
import pofol.shop.repository.CommentRepository;
import pofol.shop.service.ItemService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CommentRepository commentRepository;

    @GetMapping("/items")
    //아이템 리스트
    public String list(Model model){
        List<Item> items = itemService.findList();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/new")
    //아이템 등록폼 화면
    public String createForm(Model model){
        model.addAttribute("itemForm", new ItemForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    //아이템 등록 실행
    public String create(@Valid ItemForm form, BindingResult result){
        if(result.hasErrors()){
            return "items/createItemForm";
        }

        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAuthor(form.getAuthor());
        item.setDescription(form.getDescription());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());
        itemService.save(item);
        return "redirect:/items";
    }

    @GetMapping("/items/edit")
    //아이템 수정 폼 화면
    public String editForm(@RequestParam Long id, Model model){
        Item item = itemService.findOne(id);

        ItemForm form = new ItemForm();
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
    public String edit (@Valid ItemForm form,BindingResult result){
        System.out.println(form);
        if(result.hasErrors()){
            return "/items/updateItemForm";
        }
        itemService.edit(form.getId(), form.getItemName(), form.getPrice(), form.getQuantity());
        return "redirect:/items";
    }

    @GetMapping("/items/{itemId}")
    public String item(@PathVariable("itemId") Long id, Model model, Principal principal){
        Item item = itemService.findOne(id);
        ItemForm itemForm = new ItemForm();

        itemForm.setId(item.getId());
        itemForm.setItemName(item.getItemName());
        itemForm.setAuthor(item.getAuthor());
        itemForm.setDescription(item.getDescription());
        itemForm.setPrice(item.getPrice());
        itemForm.setQuantity(item.getQuantity());
        model.addAttribute("itemForm", itemForm);

        String userName = principal != null ? principal.getName() : "Guest";
        model.addAttribute("userName", userName);

        CommentForm commentForm = new CommentForm();
        commentForm.setItemId(id);
        commentForm.setCreatedUserName(userName);
        model.addAttribute("commentForm", commentForm);
        model.addAttribute("comments", item.getComments());
        return "/items/item";
    }

    @PostMapping("/items/createComment")
    public String createComment(@Valid CommentForm form){
        Comment comment = new Comment();
        Item item = itemService.findOne(form.getItemId());
        comment.setItem(item);
        comment.setCreatedUserName(form.getCreatedUserName());
        comment.setContent(form.getContent());
        commentRepository.save(comment);
        return "redirect:/items/" + form.getItemId();
    }
}
