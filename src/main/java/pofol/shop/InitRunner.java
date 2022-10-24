package pofol.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pofol.shop.domain.Item;
import pofol.shop.domain.enums.Role;
import pofol.shop.service.ItemService;
import pofol.shop.service.MemberService;

@Component
@RequiredArgsConstructor
public class InitRunner implements ApplicationRunner {


    private final MemberService memberService;
    private final ItemService itemService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        memberService.createMember("user", "1234", Role.ROLE_USER);
        memberService.createMember("admin", "1234", Role.ROLE_ADMIN);
        Item item1 = new Item("JAVA", 10000, 100);
        Item item2 = new Item("SPRING", 12000, 100);
        Item item3 = new Item("JPA", 15000, 100);
        itemService.saveItem(item1);
        itemService.saveItem(item2);
        itemService.saveItem(item3);

    }
}
