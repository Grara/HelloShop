package pofol.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.*;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.embedded.PersonalInfo;
import pofol.shop.domain.enums.Sex;
import pofol.shop.repository.ItemRepository;
import pofol.shop.repository.MemberRepository;
import pofol.shop.service.MemberService;
import pofol.shop.service.OrderService;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class OrderTest {
    @Autowired OrderService orderService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;
    public Item item1;
    public Item item2;
    public Member member1;

    @BeforeEach
    public void before(){
        item1 = new Item("item1", 10000, 100);
        item2 = new Item("item2", 12000, 100);
        member1 = new Member("member1", new PersonalInfo("노민준", 28, Sex.M));
        em.persist(item1);
        em.persist(item2);
        em.persist(member1);

        CartItem cartItem1 = new CartItem(member1, item1, 2);
        CartItem cartItem2 = new CartItem(member1, item2, 3);
        em.persist(cartItem1);
        em.persist(cartItem2);
    }

    @Test
    public void 장바구니주문(){
        Address address = new Address("서울", "신림", 1010);
        Member findMember = memberRepository.findMemberByUserName("member1").get(0);
        orderService.orderByCart(findMember, address);
    }

    @Test
    public void 일반주문(){
        Address address = new Address("서울", "신림", 1010);
        Member findMember = memberRepository.findMemberByUserName("member1").get(0);

        OrderItem orderItem = new OrderItem(item1, 3);
        orderService.order(findMember, address, orderItem);
    }
}
