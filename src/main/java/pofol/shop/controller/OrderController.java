package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pofol.shop.formAndDto.OrderForm;
import pofol.shop.formAndDto.OrderItemDto;
import pofol.shop.domain.*;
import pofol.shop.domain.embedded.Address;
import pofol.shop.service.CartService;
import pofol.shop.service.ItemService;
import pofol.shop.service.MemberService;
import pofol.shop.service.OrderService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;
    private final CartService cartService;
    private final MemberService memberService;

    @GetMapping("/orders/new")
    //주문 생성폼 화면
    public String createForm(@RequestParam("orderType") String type,
                             @RequestParam("itemId") Long itemId,
                             @RequestParam("count") Integer count,
                             Principal principal,
                             Model model) {

        Member member = memberService.findOneByName(principal.getName());
        OrderForm orderForm = new OrderForm();

        if (type.equals("buy")) {//바로 구매일 경우
            Item orderItem = itemService.findOne(itemId);
            OrderItemDto orderItemDto = new OrderItemDto(orderItem);
            orderItemDto.setCount(count);
            orderForm.addOrderItem(orderItemDto);
        }

        if (type.equals("cart")) {//장바구니 주문일 경우
            List<Cart> carts = cartService.findListByMemberFetchItem(member);
            List<OrderItemDto> orderItemDtos = carts.stream().map(OrderItemDto::new)
                    .collect(Collectors.toList());
            orderItemDtos.stream().forEach(orderForm::addOrderItem);
        }
        orderForm.setOrderType(type);
        orderForm.setItemId(itemId);
        model.addAttribute("orderForm", orderForm);
        return "orders/orderForm";
    }

    @PostMapping("/orders/new")
    //주문 생성 요청
    public String order(@ModelAttribute OrderForm form, Principal principal) {
        Member member = memberService.findOneByName(principal.getName());
        Address address = new Address(form.getAddress1(), form.getAddress2(), form.getZipcode());
        System.out.println("####" + form.getOrderItems());
        if (form.getOrderType().equals("buy")) {
            Item item = itemService.findOne(form.getOrderItems().get(0).getItemId());
            OrderItem orderItem = new OrderItem(item, form.getCount());
            orderService.order(member, address, orderItem);
        }

        if (form.getOrderType().equals("cart")) {
            orderService.orderByCart(member, address);
        }

        return "redirect:/";
    }

}
