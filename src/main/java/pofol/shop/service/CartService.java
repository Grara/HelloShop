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
     * 입력된 id에 해당하는 Cart를 DB에서 찾습니다.
     * @param id 찾을 Cart의 id
     * @return 찾아낸 Cart
     */
    public Cart findOne(Long id){
        return cartRepository.findById(id).orElseThrow(()->new EntityNotFoundException("cart not found"));
    }

    /**
     * 인자로 들어온 Member의 Cart리스트를 DB에서 찾습니다. <br/>
     * 각 Cart의 Item에 페치조인을 적용합니다.
     * @param member 찾을 Cart들을 지닌 Member
     * @return 해당 Member가 지닌 Cart의 List
     */
    public List<Cart> findListByMemberFetchItem(Member member){
        return cartRepository.findByMemberFetchItem(member);
    }

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

    /**
     * Cart를 DB에 저장합니다.
     * @param cart 저장할 Cart
     * @return : 저장한 Cart의 id
     */
    public Long add(Cart cart){
        cartRepository.save(cart);
        return cart.getId();
    }

    /**
     * Cart를 DB에서 삭제합니다.
     * @param cart 삭제할 Cart
     */
    public void delete(Cart cart) {
        cartRepository.delete(cart);
    }

}
