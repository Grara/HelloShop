package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Member;
import pofol.shop.dto.business.OrderItemDto;
import pofol.shop.dto.security.UserAdapter;
import pofol.shop.repository.CartRepository;
import pofol.shop.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 장바구니와 관련된 뷰를 반환하는 Controller 입니다.
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-26
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-13
 */
@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;

    /**
     * 장바구니 목록 페이지를 반환합니다.
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-26
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-13
     * @param principal : 로그인 세션 정보
     */
    @GetMapping("/cart") //Member의 장바구니 리스트
    public String list(Model model, @AuthenticationPrincipal UserAdapter principal) {

        Member member = memberRepository.findByUserName(principal.getName()).orElseThrow();
        List<Cart> carts = cartRepository.findListByMemberFetchItem(member);
        List<OrderItemDto> itemDtos = carts.stream().map(OrderItemDto::new).collect(Collectors.toList());

        model.addAttribute("carts", itemDtos);
        model.addAttribute("username", principal.getName());

        return "carts/cartList";
    }
}
