package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;
import pofol.shop.repository.CartRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;

    /**
     * member의 Cart중에 똑같은 Item의 Cart가 DB에 있는지 확인하고, 있다면 해당 Cart의 id를 반환합니다.
     * @param member 찾을 Cart를 지닌 Member
     * @param item   Cart의 Item
     * @return 찾아낸 Cart의 id를 담은 Optional, <br/> Cart가 없을경우 Optional.empty() 반환
     */
    public Optional<Long> findDuplicateCart(Member member, Item item) {
        List<Cart> findCarts = cartRepository.findByMember(member);
        for (Cart findCart : findCarts) {
            if (findCart.getItem().equals(item)) {
                return Optional.ofNullable(findCart.getId());
            }
        }
        return Optional.empty();
    }

}
