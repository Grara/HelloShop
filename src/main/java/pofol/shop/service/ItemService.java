package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.Item;
import pofol.shop.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * 모든 Item목록을 반환합니다.
     * @return Item목록
     */
    public List<Item> findList(){ return itemRepository.findAll(); }

    /**
     * id를 통해 item을 찾아낸 뒤 반환합니다.
     * @param itemId 찾을 Item의 id
     * @return 찾아낸 Item
     */
    public Item findOne(Long itemId) { return itemRepository.findById(itemId).get(); }

    /**
     * Item을 DB에 저장합니다.
     * @param item 저장할 Item
     * @return 저장한 Item의 id
     */
    public Long save(Item item) {
        itemRepository.save(item);
        return item.getId();
    }

    /**
     * id에 해당하는 Item의 정보를 수정합니다.
     * @param itemId 수정할 Item의 id
     * @param name 수정 후 적용할 상품명
     * @param price 수정 후 적용할 가격
     * @param qty 수정 후 적용할 수량
     * @return 수정한 Item의 id
     */
    public Long edit(Long itemId, String name, int price, int qty){
        Item findItem = itemRepository.findById(itemId).get();
        findItem.setItemName(name);
        findItem.setPrice(price);
        findItem.setQuantity(qty);
        return itemId;
    }
}
