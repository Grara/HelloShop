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

    public List<Item> findList(){ return itemRepository.findAll(); }
    public Item findOne(Long itemId) { return itemRepository.findById(itemId).get(); }
    public void save(Item item) {
        itemRepository.save(item);
    }

    public void edit(Long itemId, String name, int price, int qty){
        Item findItem = itemRepository.findById(itemId).get();
        findItem.setItemName(name);
        findItem.setPrice(price);
        findItem.setQuantity(qty);
    }
}
