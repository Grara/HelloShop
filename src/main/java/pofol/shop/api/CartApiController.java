package pofol.shop.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;
import pofol.shop.dto.ApiResponseBody;
import pofol.shop.form.create.CreateCartForm;
import pofol.shop.repository.CartRepository;
import pofol.shop.repository.ItemRepository;
import pofol.shop.repository.MemberRepository;
import pofol.shop.service.CartService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @PostMapping("/cart/new") //Cart 생성 요청
    public ResponseEntity<ApiResponseBody<Boolean>> addCart(@RequestBody CreateCartForm form) {
        try {
            Member member = memberRepository.findByUserName(form.getUserName()).orElseThrow();
            Item item = itemRepository.findById(form.getItemId()).orElseThrow();
            Optional<Long> existingCartId = cartService.findDuplicateCart(member, item);

            //장바구니에 현재 추가하는 아이템과 같은 아이템이 없으면 새로 추가
            if (!existingCartId.isPresent()) {
                Cart newCart = new Cart(member, item, form.getCount());
                cartRepository.save(newCart);
            }
            //있으면 기존 장바구니 아이템에 수량만 추가
            else {
                Cart existingCart = cartRepository.findById(existingCartId.get()).orElseThrow();
                existingCart.addCount(form.getCount());
                cartRepository.save(existingCart);
            }
            return ResponseEntity
                    .ok()
                    .body(new ApiResponseBody<>(HttpStatus.OK, "장바구니 추가 완료", true));

        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new ApiResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), false));
        }
    }

    @PostMapping("/cart/delete") //Cart 삭제 요청
    public ResponseEntity<ApiResponseBody<Boolean>> delete(@RequestBody List<Long> list) {
        try {
            //전달받은 Cart의 id들을 참고하여 해당 Cart들을 삭제
            for (Long cartId : list) {
                Cart cart = cartRepository.findById(cartId).orElseThrow();
                cartRepository.delete(cart);
            }
            return ResponseEntity
                    .ok()
                    .body(new ApiResponseBody<>(HttpStatus.OK, "장바구니 삭제 성공", true));

        }catch (Exception e){
            return ResponseEntity
                    .internalServerError()
                    .body(new ApiResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), false));
        }
    }
}
