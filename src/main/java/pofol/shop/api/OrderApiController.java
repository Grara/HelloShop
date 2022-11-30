package pofol.shop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pofol.shop.domain.Member;
import pofol.shop.domain.Order;
import pofol.shop.domain.OrderSheet;
import pofol.shop.domain.enums.OrderStatus;
import pofol.shop.form.create.CreateOrderSheetForm;
import pofol.shop.repository.OrderRepository;
import pofol.shop.service.MemberService;
import pofol.shop.service.OrderService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final OrderRepository orderRepository;
    private ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/orderSheet/new") //주문시트 생성 요청
    public Long createOrderSheet(@RequestBody CreateOrderSheetForm form) throws Exception {

        OrderSheet sheet = new OrderSheet();
        Member member = memberService.findOneByName(form.getUserName());

        //Form을 JSON으로 변환해서 DB에 저장
        sheet.setContent(mapper.writeValueAsString(form));
        sheet.setMember(member);
        sheet.setIsOrdered(false);
        return orderService.saveSheet(sheet);

    }

    @PostMapping("/orders/cancel") //주문취소 요청
    public ResponseEntity<String> delete(@RequestBody String id, Principal principal) {
        Order order = orderService.findOneById(Long.parseLong(id));
        System.out.println("#####"+principal.getName());
        if(!order.getMember().getUserName().equals(principal.getName())){
            return ResponseEntity
                    .badRequest()
                    .body("주문을 한 회원이 아닙니다.");
        }

        order.setStatus(OrderStatus.CANCEL);
        orderRepository.save(order);
        return ResponseEntity
                .ok()
                .body("/orders/cancel-finish");
    }
}
