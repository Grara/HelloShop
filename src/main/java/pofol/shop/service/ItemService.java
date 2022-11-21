package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;
import pofol.shop.repository.ItemRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * 모든 Item목록을 반환합니다.
     *
     * @return 모든 Item목록
     */
    public List<Item> findList() throws Exception {
        return itemRepository.findAll();
    }

    /**
     * member가 등록한 Item목록을 반환합니다.
     * @param member 상품을 등록한 Member
     * @return member가 등록한 Item 목록
     * @throws Exception
     */
    public List<Item> findListByMember(Member member) throws Exception {
        return itemRepository.findListByMember(member);
    }

    /**
     * id를 통해 item을 찾아낸 뒤 반환합니다.
     *
     * @param itemId 찾을 Item의 id
     * @return 찾아낸 Item
     */
    public Item findOne(Long itemId) throws Exception {
        return itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("item not found"));
    }

    /**
     * Item을 DB에 저장합니다.
     *
     * @param item 저장할 Item
     * @return 저장한 Item의 id
     */
    public Long save(Item item) {
        itemRepository.save(item);
        return item.getId();
    }

    /**
     * id에 해당하는 Item의 정보를 수정합니다.
     *
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
