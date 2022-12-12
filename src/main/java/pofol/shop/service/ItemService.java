package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.Item;
import pofol.shop.repository.ItemRepository;


@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;


    /**
     * id에 해당하는 Item의 정보를 수정하고 DB에 적용합니다.
     * @param itemId 수정할 Item의 id
     * @param name   수정 후 적용할 상품명
     * @param price  수정 후 적용할 가격
     * @param qty    수정 후 적용할 수량
     * @return 수정한 Item의 id
     */
    public Long edit(Long itemId, String name, int price, int qty) {
        Item findItem = itemRepository.findById(itemId).get();
        findItem.setItemName(name);
        findItem.setPrice(price);
        findItem.setQuantity(qty);
        return itemId;
    }
}
