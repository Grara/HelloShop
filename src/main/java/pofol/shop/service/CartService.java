package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;
import pofol.shop.repository.CartRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;

    public List<Cart> findList(){
        return cartRepository.findAll();
    }
    public Long add(Cart cart){
        cartRepository.save(cart);
        return cart.getId();
    }
    public List<Cart> findListByMemberFetchItem(Member member) {
        return cartRepository.findByMemberFetchItem(member);
    }

    public List<Cart> findListFetchItem(){
        return cartRepository.findAllFetchItem();
    }

    public Optional<Cart> findOneByItem(Item item){ return cartRepository.findByItem(item); }
}
