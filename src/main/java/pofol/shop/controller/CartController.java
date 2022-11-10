package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;
import pofol.shop.formAndDto.OrderItemDto;
import pofol.shop.service.CartService;
import pofol.shop.service.ItemService;
import pofol.shop.service.MemberService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/cart")
    public String cart(Model model, Principal principal){
        Member findMember = memberService.findOneByName(principal.getName());
        List<Cart> carts = cartService.findListByMemberFetchItem(findMember);
        List<OrderItemDto> itemDtos = carts.stream().map(OrderItemDto::new).collect(Collectors.toList());
        model.addAttribute("carts", itemDtos);
        model.addAttribute("username", principal.getName());
        return "carts/cartList";
    }
}
