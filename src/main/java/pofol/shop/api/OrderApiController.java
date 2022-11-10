package pofol.shop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pofol.shop.domain.OrderSheet;
import pofol.shop.formAndDto.OrderSheetForm;
import pofol.shop.repository.OrderSheetRepository;
import pofol.shop.service.OrderService;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private ObjectMapper mapper = new ObjectMapper();


    @PostMapping("/orders/createOrderSheet")
    public Long createOrderSheet(@RequestBody OrderSheetForm form) throws Exception {

            System.out.println("$$$$"+form);
            OrderSheet sheet = new OrderSheet();
            sheet.setContent(mapper.writeValueAsString(form));
            System.out.println("$$$$"+mapper.writeValueAsString(form));
            return orderService.saveSheet(sheet);

    }
}
