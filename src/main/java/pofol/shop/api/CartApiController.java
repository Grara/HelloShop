package pofol.shop.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;
import pofol.shop.formAndDto.CartCreateForm;
import pofol.shop.formAndDto.OrderSheetForm;
import pofol.shop.service.CartService;
import pofol.shop.service.ItemService;
import pofol.shop.service.MemberService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;
    private final MemberService memberService;
    private final ItemService itemService;

    @PostMapping("/cart/new")
    public boolean addCart(@RequestBody CartCreateForm form) {
        try {
            Member member = memberService.findOneByName(form.getUserName());
            Item item = itemService.findOne(form.getItemId());
            Optional<Long> existingCartId = cartService.duplicateCheck(member, item);

            //장바구니에 현재 추가하는 아이템과 같은 아이템이 없으면 새로 추가
            if (!existingCartId.isPresent()) {
                Cart newCart = new Cart(member, item, form.getCount());
                cartService.add(newCart);
            }
            //있으면 기존 장바구니 아이템에 수량만 추가
            else {
                Cart existingCart = cartService.findOne(existingCartId.get());
                existingCart.addCount(form.getCount());
                cartService.add(existingCart);
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping("/cart/delete")
    public boolean delete(@RequestBody List<Long> list) throws Exception{
        for(Long cartId : list){
            Cart cart = cartService.findOne(cartId);
            cartService.delete(cart);
        }
        return true;
    }
}
