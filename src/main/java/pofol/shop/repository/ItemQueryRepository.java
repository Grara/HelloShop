package pofol.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pofol.shop.dto.ItemDto;
import pofol.shop.dto.ItemSearchCondition;


public interface ItemQueryRepository {

    Page<ItemDto> searchWithPage(ItemSearchCondition condition, Pageable pageable);
}
