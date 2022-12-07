package pofol.shop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pofol.shop.domain.Member;
import pofol.shop.domain.Order;
import pofol.shop.domain.OrderSheet;
import pofol.shop.domain.enums.OrderStatus;
import pofol.shop.dto.ApiResponseBody;
import pofol.shop.form.create.CreateOrderSheetForm;
import pofol.shop.repository.MemberRepository;
import pofol.shop.repository.OrderRepository;
import pofol.shop.repository.OrderSheetRepository;
import pofol.shop.service.MemberService;
import pofol.shop.service.OrderService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final OrderSheetRepository orderSheetRepository;
    private ObjectMapper mapper = new ObjectMapper(); //JSON - 객체 간 매퍼

    @PostMapping("/orderSheet/new") //주문시트 생성 요청
    public ResponseEntity<ApiResponseBody<Long>> createOrderSheet(@RequestBody CreateOrderSheetForm form) throws Exception {

        try {
            OrderSheet sheet = new OrderSheet();
            Member member = memberRepository.findByUserName(form.getUserName()).orElseThrow();

            //Form을 JSON으로 변환해서 DB에 저장

            sheet.setContent(mapper.writeValueAsString(form));
            sheet.setMember(member);
            sheet.setIsOrdered(false);
            Long id = orderSheetRepository.save(sheet).getId();

            return ResponseEntity
                    .ok()
                    .body(new ApiResponseBody<>(HttpStatus.OK, "주문시트 생성 완료", id));


        }catch (Exception e){
            return ResponseEntity
                    .internalServerError()
                    .body(new ApiResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        }

    }

    @PostMapping("/orders/cancel") //주문취소 요청
    public ResponseEntity<ApiResponseBody<String>> delete(@RequestBody String id, Principal principal) {
        try {
            Order order = orderRepository.findById(Long.parseLong(id)).orElseThrow();
            if (!order.getMember().getUserName().equals(principal.getName())) {
                return ResponseEntity
                        .badRequest()
                        .body(new ApiResponseBody<>(HttpStatus.FORBIDDEN, "주문을 한 회원이 아닙니다", null));
            }

            order.setStatus(OrderStatus.CANCEL);
            orderRepository.save(order);
            return ResponseEntity
                    .ok()
                    .body(new ApiResponseBody<>(HttpStatus.OK, "취소 성공", "/orders/cancel-finish"));
        }catch (Exception e){
            return ResponseEntity
                    .internalServerError()
                    .body(new ApiResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR, "주문을 한 회원이 아닙니다", null));
        }
    }
}
