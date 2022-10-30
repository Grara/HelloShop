package pofol.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;
import pofol.shop.domain.enums.Role;
import pofol.shop.service.CartService;
import pofol.shop.service.ItemService;
import pofol.shop.service.MemberService;

@Component
@RequiredArgsConstructor
public class InitRunner implements ApplicationRunner {


    private final MemberService memberService;
    private final ItemService itemService;
    private final CartService cartService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Long userId = memberService.createMember("user", "1234", Role.ROLE_USER);
        memberService.createMember("admin", "1234", Role.ROLE_ADMIN);

        Item item1 = new Item("JAVA", 10000, 100);
        Item item2 = new Item("SPRING", 12000, 100);
        Item item3 = new Item("JPA", 15000, 100);
        itemService.saveItem(item1);
        itemService.saveItem(item2);
        itemService.saveItem(item3);

        Member findMember = memberService.findMemberById(userId);

        Cart cart1 = new Cart(findMember, item1, 1);
        Cart cart2 = new Cart(findMember, item2, 2);
        Cart cart3 = new Cart(findMember, item3, 3);
        cartService.saveCart(cart1);
        cartService.saveCart(cart2);
        cartService.saveCart(cart3);
    }
}
