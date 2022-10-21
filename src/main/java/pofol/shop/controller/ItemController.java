package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pofol.shop.controller.form.ItemForm;
import pofol.shop.domain.Item;
import pofol.shop.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items")
    //아이템 리스트
    public String list(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("itemForm", new ItemForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
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
        itemService.saveItem(item);
        return "redirect:/items";
    }

    @GetMapping("/items/edit")
    public String editForm(@RequestParam("id") Long itemId, Model model){
        Item item = itemService.findOne(itemId);

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
    public String edit (@Valid ItemForm form,BindingResult result){
        if(result.hasErrors()){
            return "/items/updateItemForm";
        }
        itemService.updateItem(form.getId(), form.getItemName(), form.getPrice(), form.getQuantity());
        return "redirect:/items";
    }
}
