package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.*;
import pofol.shop.domain.embedded.Address;
import pofol.shop.dto.OrderItemDto;
import pofol.shop.dto.OrderSearchCondition;
import pofol.shop.exception.NotEnoughQuantityException;
import pofol.shop.repository.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final OrderItemRepository orderItemRepository;
    private final OrderSheetRepository orderSheetRepository;
    private final ItemService itemService;

    /**
     * 장바구니에서 주문을 생성합니다.
     * @param member 주문한 Member
     * @param address 주소정보
     * @return 생성한 주문의 id
     */
    public long orderByCart(Member member, Address address, List<OrderItemDto> itemDtos) throws Exception {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Cart> orderedCart = new ArrayList<>();
        for(OrderItemDto dto : itemDtos){
            Cart cart = cartService.findOne(dto.getCartId());
            orderedCart.add(cart);
        }

        for (Cart cart : orderedCart) {
            OrderItem orderItem = new OrderItem(cart);
            orderItems.add(orderItem);
            orderItemRepository.save(orderItem);
            cartService.delete(cart);
        }
        Order order = Order.createOrder(member, address, orderItems);
        orderRepository.save(order);
        return order.getId();
    }

    /**
     * 바로구매 시 주문을 생성합니다.
     * @param member 주문한 Member
     * @param address 주소정보
     * @param itemDtos 주문 시 참고할 ItemDto 리스트
     * @return 생성한 주문의 id
     */
    public long order(Member member, Address address, List<OrderItemDto> itemDtos) throws Exception{
        List<OrderItem> orderItems = new ArrayList<>();
        for(OrderItemDto dto : itemDtos){
            if(dto.getCartId() != null) {
                Cart cart = cartService.findOne(dto.getCartId());
                cart.getItem().reduceQty(cart.getCount());
                OrderItem orderItem = new OrderItem(cart);
                orderItemRepository.save(orderItem);
                orderItems.add(orderItem);
                cartService.delete(cart);
            }
            else{
                Item item = itemService.findOne(dto.getItemId());
                item.reduceQty(dto.getCount());
                OrderItem orderItem = new OrderItem(item, dto.getCount());
                orderItemRepository.save(orderItem);
                orderItems.add(orderItem);
            }
        }
        
        Order order = Order.createOrder(member, address, orderItems);
        orderRepository.save(order);
        return order.getId();
    }

    public long saveSheet(OrderSheet sheet){
        orderSheetRepository.save(sheet);
        return sheet.getId();
    }

    public OrderSheet findSheetById(Long id) throws Exception{
        return orderSheetRepository.findById(id).orElseThrow(()->new EntityNotFoundException("sheet not found"));
    }

    public List<Order> search(OrderSearchCondition condition){
        return orderRepository.search(condition);
    }

}
