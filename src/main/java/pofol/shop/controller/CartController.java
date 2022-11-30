package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Member;
import pofol.shop.dto.OrderItemDto;
import pofol.shop.service.CartService;
import pofol.shop.service.ItemService;
import pofol.shop.service.MemberService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/cart") //Member의 장바구니 리스트
    public String list(Model model, Principal principal) {

        Member member = memberService.findOneByName(principal.getName());
        List<Cart> carts = cartService.findListByMemberFetchItem(member);
        List<OrderItemDto> itemDtos = carts.stream().map(OrderItemDto::new).collect(Collectors.toList());

        model.addAttribute("carts", itemDtos);
        model.addAttribute("username", principal.getName());

        return "carts/cartList";

    }
}
