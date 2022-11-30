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
     * 인자로 들어온 id에 해당하는 Order를 DB에서 찾습니다.
     * @param id 찾아낼 Order의 id
     * @return 찾아낸 Order
     */
    public Order findOneById(Long id){
        return orderRepository.findById(id).orElseThrow(()->new EntityNotFoundException());
    }

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
        
        Order order = Order.createOrder(member, delivery, orderItems);
        orderRepository.save(order);
        return order.getId();
    }

    /**
     * id에 해당하는 주문시트를 DB에서 찾습니다.
     * @param id 찾을 주문시트의 id
     * @return 찾아낸 주문시트
     */
    public OrderSheet findSheetById(Long id){
        return orderSheetRepository.findById(id).orElseThrow(()->new EntityNotFoundException("sheet not found"));
    }

    /**
     * 주문시트를 DB에 저장합니다.
     * @param sheet 저장할 주문시트
     * @return 저장한 주문시트의 id
     */
    public long saveSheet(OrderSheet sheet){
        orderSheetRepository.save(sheet);
        return sheet.getId();
    }

    /**
     * 검색조건에 해당하는 Order들을 DB에서 찾습니다.
     * @param condition 검색조건
     * @return 찾아낸 Order들의 List
     */
    public List<Order> search(OrderSearchCondition condition){
        return orderRepository.search(condition);
    }

}
