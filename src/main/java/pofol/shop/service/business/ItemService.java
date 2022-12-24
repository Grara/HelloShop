package pofol.shop.service.business;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.Item;
import pofol.shop.repository.ItemRepository;

import java.util.NoSuchElementException;

/**
 * 상품과 관련된 비즈니스로직을 처리하는 Service클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-07
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * id에 해당하는 Item을 찾습니다. <br/>
     * 컨트롤러에서 Optional처리하기 귀찮아서 만듬
     *
     * @param id 찾을 Cart의 id
     * @return 찾아낸 Item
     * @throws NoSuchElementException id에 해당하는 Cart가 없을 경우
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-24
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-24
     */
    public Item findById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("id에 해당하는 Item을 찾지못했습니다. id : " + id));
    }

    /**
     * id에 해당하는 Item을 찾아내며, 상품후기와 후기작성 회원까지 페치조인합니다. <br/>
     * 컨트롤러에서 Optional처리하기 귀찮아서 만듬
     *
     * @param id 찾을 Cart의 id
     * @return 찾아낸 Item
     * @throws NoSuchElementException id에 해당하는 Cart가 없을 경우
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-24
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-24
     */
    public Item findByIdFetchCommentsWithMember(Long id) {
        return itemRepository.findByIdFetchCommentsWithMember(id)
                .orElseThrow(() -> new NoSuchElementException("id에 해당하는 Item을 찾지못했습니다. id : " + id));
    }

    /**
     * id에 해당하는 Item의 정보를 수정하고 DB에 적용합니다.
     *
     * @param itemId 수정할 Item의 id
     * @param name   수정 후 적용할 상품명
     * @param price  수정 후 적용할 가격
     * @param qty    수정 후 적용할 수량
     * @return 수정한 Item의 id
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-07
     */
    public Long edit(Long itemId, String name, int price, int qty) {
        Item findItem = itemRepository.findById(itemId).get();
        findItem.setItemName(name);
        findItem.setPrice(price);
        findItem.setQuantity(qty);
        return itemId;
    }
}
