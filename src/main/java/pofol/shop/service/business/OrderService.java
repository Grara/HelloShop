package pofol.shop.service.business;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.*;
import pofol.shop.dto.business.OrderItemDto;
import pofol.shop.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 주문과 관련된 비즈니스로직을 처리하는 Service클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-24
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final ItemService itemService;

    /**
     * ID에 해당하는 Order를 DB에서 찾습니다.
     *
     * @param id 찾을 Order의 ID
     * @return 찾은 Order
     * @throws NoSuchElementException id에 해당하는 Order가 없을 경우
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-24
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-24
     */
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("id에 해당하는 주문이 없습니다. 입력한 id : " + id));
    }

    /**
     * 주어진 인자로 주문을 생성 후 DB에 저장합니다.
     *
     * @param member   주문한 Member
     * @param delivery 배송정보
     * @param itemDtos 주문한 Item들의 DTO 리스트
     * @return 생성한 주문의 id
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-30
     */
    public long order(Member member, Delivery delivery, List<OrderItemDto> itemDtos) {

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDto dto : itemDtos) {
            if (dto.getCartId() != null) {
                Cart cart = cartService.findById(dto.getCartId());
                cart.getItem().reduceQty(cart.getCount());
                OrderItem orderItem = new OrderItem(cart);
                orderItemRepository.save(orderItem);
                orderItems.add(orderItem);
                cartRepository.delete(cart);
            } else {
                Item item = itemService.findById(dto.getItemId());
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
