package pofol.shop.service.business;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;
import pofol.shop.repository.CartRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * 장바구니와 관련된 비즈니스로직을 처리하는 Service클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-30
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-24
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;

    /**
     * member의 Cart중에 똑같은 Item의 Cart가 DB에 있는지 확인하고, 있다면 해당 Cart의 id를 반환합니다.
     *
     * @param member 찾을 Cart를 지닌 Member
     * @param item   Cart의 Item
     * @return 찾아낸 Cart의 id를 담은 Optional, <br/> Cart가 없을경우 Optional.empty() 반환
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-24
     */
    public Optional<Long> findDuplicateCart(Member member, Item item) {
        List<Cart> findCarts = cartRepository.findListByMember(member);
        for (Cart findCart : findCarts) {
            if (findCart.getItem().equals(item)) {
                return Optional.ofNullable(findCart.getId());
            }
        }
        return Optional.empty();
    }

    /**
     * ID에 해당하는 Cart를 DB에서 찾습니다.
     *
     * @param id 찾을 Cart의 ID
     * @return 찾은 Cart
     * @throws NoSuchElementException id에 해당하는 Cart가 없을 경우
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-24
     */
    public Cart findById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("id에 해당하는 Cart가 없습니다. 찾으려한 Cart ID : " + id));
    }

    /**
     * ID에 해당하는 Cart를 DB에서 찾고 각 Cart의 Member를 페치조인합니다.
     *
     * @param id 찾을 Cart의 ID
     * @return 찾은 Cart
     * @throws NoSuchElementException id에 해당하는 Cart가 없을 경우
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-24
     */
    public Cart findByIdFetchMember(Long id) {
        return cartRepository.findByIdFetchMember(id)
                .orElseThrow(() -> new NoSuchElementException("id에 해당하는 Cart가 없습니다. 찾으려한 Cart ID : " + id));
    }
}
