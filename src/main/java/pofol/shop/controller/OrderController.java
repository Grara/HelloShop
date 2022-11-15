package pofol.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pofol.shop.formAndDto.OrderForm;
import pofol.shop.formAndDto.OrderItemDto;
import pofol.shop.domain.*;
import pofol.shop.domain.embedded.Address;
import pofol.shop.formAndDto.OrderSheetForm;
import pofol.shop.repository.OrderSheetRepository;
import pofol.shop.service.CartService;
import pofol.shop.service.ItemService;
import pofol.shop.service.MemberService;
import pofol.shop.service.OrderService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        System.out.println("start");

        if (sheet.getIsOrdered() || !findMember.getUserName().equals(principal.getName())){
            return "redirect:/";
        }
        OrderSheetForm content = mapper.readValue(sheet.getContent(), OrderSheetForm.class);

        OrderForm orderForm = new OrderForm(content.getItems());
        orderForm.setSheetId(sheet.getId());
        model.addAttribute("orderForm", orderForm);

        return "orders/orderForm";
    }

    @PostMapping("/orders/new")
    //주문 생성 요청
    public String order(@ModelAttribute OrderForm form, Principal principal) throws Exception {
        Address address = new Address(form.getAddress1(), form.getAddress2(), form.getZipcode());
        Member member = memberService.findOneByName(principal.getName());
        orderService.order(member, address, form.getOrderItems());
        OrderSheet sheet = orderService.findSheetById(form.getSheetId());
        sheet.setIsOrdered(true);
        orderService.saveSheet(sheet);

        return "redirect:/";
    }
}
