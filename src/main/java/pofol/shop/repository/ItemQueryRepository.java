package pofol.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pofol.shop.dto.business.ItemDto;
import pofol.shop.dto.business.ItemSearchCondition;

/**
 * ItemQueryRepositoryImpl을 만들기 위한 인터페이스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-07
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-07
 */
public interface ItemQueryRepository {

    Page<ItemDto> searchWithPage(ItemSearchCondition condition, Pageable pageable);
}
