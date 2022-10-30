package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pofol.shop.controller.form.OrderForm;
import pofol.shop.controller.form.OrderItemDto;
import pofol.shop.domain.*;
import pofol.shop.domain.embedded.Address;
import pofol.shop.repository.CartRepository;
import pofol.shop.service.ItemService;
import pofol.shop.service.MemberService;
import pofol.shop.service.OrderService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;
    private final CartRepository cartRepository;
    private final MemberService memberService;
    @GetMapping("/orders/new")
    public String createForm(@RequestParam("orderType") String type,
                              @RequestParam("itemId") Long itemId,
                              @RequestParam("count") Integer count,
                              Principal principal,
                              Model model) {

        Member member = memberService.findMemberByName(principal.getName());
        OrderForm orderForm = new OrderForm();

        if (type.equals("buy")) {//바로 구매일 경우
            Item orderItem = itemService.findOne(itemId);
            orderForm.setItemId(itemId);
            orderForm.setCount(count);
            OrderItemDto orderItemDto = new OrderItemDto(orderItem);
            orderItemDto.setCount(count);
            orderForm.addOrderItem(orderItemDto);
        }

        if(type.equals("cart")){//장바구니 주문일 경우
            List<Cart> carts = cartRepository.findByMemberFetchItem(member);
            List<OrderItemDto> orderItemDtos = carts.stream().map(OrderItemDto::new)
                                                .collect(Collectors.toList());
            orderItemDtos.stream().forEach(orderForm::addOrderItem);
        }
        orderForm.setOrderType(type);
        model.addAttribute("orderForm", orderForm);
        return "orders/orderForm";
    }

    @PostMapping("/orders/new")
    public String order(@Valid OrderForm form, BindingResult bindingResult, Principal principal){
        System.out.println("####"+form);
        Member member = memberService.findMemberByName(principal.getName());
        Address address = new Address(form.getAddress1(), form.getAddress2(), form.getZipcode());

        if(form.getOrderType().equals("buy")){
            Item item = itemService.findOne(form.getItemId());
            OrderItem orderItem = new OrderItem(item, form.getCount());
            orderService.order(member, address, orderItem);
        }

        if(form.getOrderType().equals("cart")){
            orderService.orderByCart(member, address);
        }

        return "redirect:/";
    }
}
