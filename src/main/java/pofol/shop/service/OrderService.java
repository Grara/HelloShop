package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.*;
import pofol.shop.domain.embedded.Address;
import pofol.shop.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderSheetRepository orderSheetRepository;

    /**
     * 장바구니에서 주문을 생성합니다.
     * @param member 주문한 Member
     * @param address 주소정보
     * @return 생성한 주문의 id
     */
    public long orderByCart(Member member, Address address){
        List<Cart> findCart = cartRepository.findByMember(member);
        List<OrderItem> orderItems = new ArrayList<>();
        for (Cart cart : findCart) {
            OrderItem orderItem = new OrderItem(cart);
            orderItems.add(orderItem);
            orderItemRepository.save(orderItem);
            cartRepository.delete(cart);
        }
        Order order = Order.createOrder(member, address, orderItems);
        orderRepository.save(order);
        return order.getId();
    }

    /**
     * 바로구매 시 주문을 생성합니다.
     * @param member 주문한 Member
     * @param address 주소정보
     * @param orderItem 주문한 Item
     * @return 생성한 주문의 id
     */
    public long order(Member member, Address address, OrderItem orderItem){
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        orderItemRepository.save(orderItem);
        Order order = Order.createOrder(member, address, orderItems);
        orderRepository.save(order);
        return order.getId();
    }

    public long saveSheet(OrderSheet sheet){
        orderSheetRepository.save(sheet);
        return sheet.getId();
    }

    public Optional<OrderSheet> findSheetById(Long id){
        return orderSheetRepository.findById(id);
    }

}
