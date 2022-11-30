package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.*;
import pofol.shop.dto.OrderItemDto;
import pofol.shop.repository.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;

    /**
     * 주어진 인자로 주문을 생성 후 DB에 저장합니다.
     * @param member 주문한 Member
     * @param delivery 배송정보
     * @param itemDtos 주문한 Item들의 DTO 리스트
     * @return 생성한 주문의 id
     */
    public long order(Member member, Delivery delivery, List<OrderItemDto> itemDtos){

        List<OrderItem> orderItems = new ArrayList<>();
        for(OrderItemDto dto : itemDtos){
            if(dto.getCartId() != null) {
                Cart cart = cartRepository.findById(dto.getCartId()).orElseThrow(()->new EntityNotFoundException("Cart not found"));
                cart.getItem().reduceQty(cart.getCount());
                OrderItem orderItem = new OrderItem(cart);
                orderItemRepository.save(orderItem);
                orderItems.add(orderItem);
                cartRepository.delete(cart);
            }
            else{
                Item item = itemRepository.findById(dto.getItemId()).orElseThrow(()->new EntityNotFoundException("Item not found"));
                item.reduceQty(dto.getCount());
                OrderItem orderItem = new OrderItem(item, dto.getCount());
                orderItemRepository.save(orderItem);
                orderItems.add(orderItem);
            }
        }
        
        Order order = Order.createOrder(member, delivery, orderItems);
        orderRepository.save(order);
        return order.getId();
    }

}
