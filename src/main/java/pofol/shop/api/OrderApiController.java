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

    @PostMapping("/orderSheet/new")
    public Long createOrderSheet(@RequestBody OrderSheetForm form) throws Exception {

            OrderSheet sheet = new OrderSheet();
            sheet.setContent(mapper.writeValueAsString(form));
            sheet.setIsOrdered(false);
            return orderService.saveSheet(sheet);

    }
}
