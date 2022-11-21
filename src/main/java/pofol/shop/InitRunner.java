package pofol.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pofol.shop.config.DefaultValue;
import pofol.shop.domain.Cart;
import pofol.shop.domain.FileEntity;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;
import pofol.shop.domain.enums.Role;
import pofol.shop.service.CartService;
import pofol.shop.service.FileService;
import pofol.shop.service.ItemService;
import pofol.shop.service.MemberService;

@Component
@RequiredArgsConstructor
public class InitRunner implements ApplicationRunner {


    private final MemberService memberService;
    private final ItemService itemService;
    private final CartService cartService;
    private final FileService fileService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        FileEntity file1 = new FileEntity();
        file1.setSavePath("C:/test/DEFAULT_PROFILE_IMAGE.jpeg");
        DefaultValue.DEFAULT_PROFILE_IMAGE_ID = fileService.initFile(file1);

        FileEntity file2 = new FileEntity();
        file2.setSavePath("C:/test/DEFAULT_ITEM_THUMBNAIL.png");
        DefaultValue.DEFAULT_ITEM_THUMBNAIL_ID = fileService.initFile(file2);

        Long userId = memberService.createInitMember("user", "1234", Role.ROLE_USER);
        memberService.createInitMember("admin", "1234", Role.ROLE_ADMIN);
        Member findMember = memberService.findOne(userId);

        Item item1 = new Item(findMember,"JAVA", 10000, 100);
        Item item2 = new Item(findMember,"SPRING", 12000, 100);
        Item item3 = new Item(findMember,"JPA", 15000, 100);
        Item item4 = new Item("JPA", 15000, 100);
        itemService.save(item1);
        itemService.save(item2);
        itemService.save(item3);
        itemService.save(item4);


        Cart cart1 = new Cart(findMember, item1, 1);
        Cart cart2 = new Cart(findMember, item2, 2);
        Cart cart3 = new Cart(findMember, item3, 3);
        cartService.add(cart1);
        cartService.add(cart2);
        cartService.add(cart3);
    }
}
