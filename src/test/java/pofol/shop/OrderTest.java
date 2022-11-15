package pofol.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.*;
import pofol.shop.domain.embedded.Address;
import pofol.shop.repository.MemberRepository;
import pofol.shop.service.OrderService;

import javax.persistence.EntityManager;

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
        member1 = new Member("member1", "1234");
        em.persist(item1);
        em.persist(item2);
        em.persist(member1);

        Cart cart1 = new Cart(member1, item1, 2);
        Cart cart2 = new Cart(member1, item2, 3);
        em.persist(cart1);
        em.persist(cart2);
    }

    @Test
    public void 장바구니주문(){
        Address address = new Address("서울", "신림", 1010);
        Member findMember = memberRepository.findByUserName("member1").get();
    }

    @Test
    public void 일반주문(){
        Address address = new Address("서울", "신림", 1010);
        Member findMember = memberRepository.findByUserName("member1").get();

        OrderItem orderItem = new OrderItem(item1, 3);
    }
}
