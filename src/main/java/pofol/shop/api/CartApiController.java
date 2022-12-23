package pofol.shop.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pofol.shop.domain.Cart;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;
import pofol.shop.dto.ApiResponseBody;
import pofol.shop.dto.security.UserAdapter;
import pofol.shop.form.create.CreateCartForm;
import pofol.shop.repository.CartRepository;
import pofol.shop.repository.ItemRepository;
import pofol.shop.repository.MemberRepository;
import pofol.shop.service.CartService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * 장바구니와 관련된 API요청을 처리해주는 Controller 클래스입니다.
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-09
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-23
 */
@RestController
@RequiredArgsConstructor
public class CartApiController {

    private final CartRepository cartRepository;
    private final CartService cartService;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 장바구니에 상품을 추가합니다.
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-09
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-23
     * @param form      : Cart생성에 필요한 데이터를 담고있는 폼
     * @param principal : 로그인 세션 정보
     * @return : 생성 성공 여부
     */
    @PostMapping("/cart") //Cart 생성 요청
    public ResponseEntity<ApiResponseBody<Boolean>> addCart(@RequestBody CreateCartForm form,
                                                            @AuthenticationPrincipal UserAdapter principal) {

        try {
            Member member = memberRepository.findByUserName(form.getUserName()).orElseThrow();

            //Cart를 생성하려는 회원과 요청을 보낸 로그인 회원이 다를 때
            if (principal == null || !member.getUserName().equals(principal.getUsername())) {
                throw new AuthenticationException("다른 회원의 Cart를 임의로 생성할 수 없습니다") {
                };
            }

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

        } catch (NoSuchElementException e) { //Repository에서 가져온 Optional중에 null이 있을 경우
            return ResponseEntity
                    .internalServerError()
                    .body(new ApiResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), false));

        } catch (AuthenticationException e) { //Cart를 생성하려는 회원과 요청을 보낸 로그인한 회원이 다를 때
            return ResponseEntity
                    .internalServerError()
                    .body(new ApiResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), false));
        }
    }

    /**
     * 장바구니에서 아이템을 삭제합니다.
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-14
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-23
     * @param list : 삭제하려는 Cart들의 id 리스트
     * @param principal : 로그인 세션 정보
     * @return : 삭제 성공 여부
     */
    @PostMapping("/cart/delete") //Cart 삭제 요청
    public ResponseEntity<ApiResponseBody<Boolean>> delete(@RequestBody List<Long> list,
                                                           @AuthenticationPrincipal UserAdapter principal) {
        System.out.println("################################################################");
        System.out.println(principal);

        try {
            //전달받은 Cart의 id들을 참고하여 해당 Cart들을 삭제
            for (Long cartId : list) {
                Cart cart = cartRepository.findByIdFetchMember(cartId).orElseThrow();

                //삭제하려는 Cart의 회원과 요청을 보낸 로그인 회원이 다르다면
                if (principal == null || !cart.getMember().getUserName().equals(principal.getUsername())) {
                    throw new AuthenticationException("다른 회원의 Cart를 임의로 생성할 수 없습니다") {
                    };
                }
                cartRepository.delete(cart);
            }
            return ResponseEntity
                    .ok()
                    .body(new ApiResponseBody<>(HttpStatus.OK, "장바구니 삭제 성공", true));

        } catch (NoSuchElementException e) { //존재하지않는 Cart를 삭제하려고한다면
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponseBody<>(HttpStatus.BAD_REQUEST, e.getMessage(), false));

        } catch (AuthenticationException e) { //삭제하려는 Cart의 회원과 요청을 보낸 로그인 회원이 다르다면
            return ResponseEntity
                    .internalServerError()
                    .body(new ApiResponseBody<>(HttpStatus.FORBIDDEN, e.getMessage(), false));
        }
    }
}
