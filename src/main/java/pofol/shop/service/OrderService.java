package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.*;
import pofol.shop.domain.embedded.Address;
import pofol.shop.repository.CartRepository;
import pofol.shop.repository.MemberRepository;
import pofol.shop.repository.OrderItemRepository;
import pofol.shop.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;

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

    public long order(Member member, Address address, OrderItem orderItem){
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        orderItemRepository.save(orderItem);
        Order order = Order.createOrder(member, address, orderItems);
        orderRepository.save(order);
        return order.getId();
    }
}
