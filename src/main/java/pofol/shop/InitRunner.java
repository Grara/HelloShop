package pofol.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pofol.shop.domain.Item;
import pofol.shop.service.ItemService;
import pofol.shop.service.MemberService;

import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class InitRunner implements ApplicationRunner {


    private final MemberService memberService;
    private final ItemService itemService;




    @Override
    public void run(ApplicationArguments args) throws Exception {
        memberService.initMember("user1", "1234");
        memberService.initMember("user2", "1234");
        Item item1 = new Item("JAVA", 10000, 100);
        Item item2 = new Item("SPRING", 12000, 100);
        Item item3 = new Item("JPA", 15000, 100);
        itemService.saveItem(item1);
        itemService.saveItem(item2);
        itemService.saveItem(item3);

    }
}
