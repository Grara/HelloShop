package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.Cart;
import pofol.shop.repository.CartRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;

    public Long saveCart(Cart cart){
        cartRepository.save(cart);
        return cart.getId();
    }
}
