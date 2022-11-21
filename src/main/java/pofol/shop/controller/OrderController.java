package pofol.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pofol.shop.dto.OrderSearchCondition;
import pofol.shop.exception.NotEnoughQuantityException;
import pofol.shop.form.create.CreateOrderForm;
import pofol.shop.domain.*;
import pofol.shop.domain.embedded.Address;
import pofol.shop.form.create.CreateOrderSheetForm;
import pofol.shop.service.CartService;
import pofol.shop.service.ItemService;
import pofol.shop.service.MemberService;
import pofol.shop.service.OrderService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;
    private final CartService cartService;
    private final MemberService memberService;
    private ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/orderSheet/{id}")
    //주문 생성폼 화면
    public String createForm(@PathVariable("id") Long id,
                             Model model,
                             Principal principal) throws Exception {

        OrderSheet sheet = orderService.findSheetById(id);
        Member findMember = sheet.getMember();

        if (sheet.getIsOrdered() || !findMember.getUserName().equals(principal.getName())){
            return "redirect:/";
        }
        CreateOrderSheetForm content = mapper.readValue(sheet.getContent(), CreateOrderSheetForm.class);

        CreateOrderForm orderForm = new CreateOrderForm(content.getItems());
        orderForm.setSheetId(sheet.getId());
        model.addAttribute("createOrderForm", orderForm);

        return "orders/orderForm";
    }

    @PostMapping("/orders/new")
    //주문 생성 요청
    public String order(@ModelAttribute @Valid CreateOrderForm form, BindingResult result, Principal principal) throws Exception {
        if(result.hasErrors()) {
            return "orders/orderForm";
        }
        try {
            Address address = new Address(form.getAddress1(), form.getAddress2(), form.getZipcode());
            Member member = memberService.findOneByName(principal.getName());
            orderService.order(member, address, form.getOrderItems());
            OrderSheet sheet = orderService.findSheetById(form.getSheetId());
            sheet.setIsOrdered(true);
            orderService.saveSheet(sheet);
        }catch(NotEnoughQuantityException e){
            return "errors/quantityError";
        }

        return "redirect:/";
    }

    @GetMapping("/mypage/myorders")
    public String myorders(@ModelAttribute OrderSearchCondition condition, Model model,Principal principal) throws Exception {
        System.out.println(condition);
        Member member = memberService.findOneByName(principal.getName());
        condition.setMember(member);
        List<Order> orders = orderService.search(condition);
        model.addAttribute("orderSearch", condition);
        model.addAttribute("orders", orders);
        return "/mypage/myorders";
    }
}
