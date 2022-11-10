package pofol.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pofol.shop.formAndDto.OrderForm;
import pofol.shop.formAndDto.OrderItemDto;
import pofol.shop.domain.*;
import pofol.shop.domain.embedded.Address;
import pofol.shop.formAndDto.OrderSheetForm;
import pofol.shop.repository.OrderSheetRepository;
import pofol.shop.service.CartService;
import pofol.shop.service.ItemService;
import pofol.shop.service.MemberService;
import pofol.shop.service.OrderService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;
    private final CartService cartService;
    private final MemberService memberService;
    private ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/orders/sheet/{id}")
    //주문 생성폼 화면
    public String createForm(@PathVariable("id") Long id,
                             Model model,
                             Principal principal) throws Exception {


            Optional<OrderSheet> opSheet = orderService.findSheetById(id);
        System.out.println("111111");
            if (opSheet.isPresent()) {
                OrderSheet sheet = opSheet.get();
                OrderSheetForm sheetForm = mapper.readValue(sheet.getContent(), OrderSheetForm.class);
                System.out.println(sheetForm);
                System.out.println("2222222");
                if(!sheetForm.getUserName().equals(principal.getName())){
                    return "orders/orderForm";
                }

                System.out.println("333333");
                OrderForm orderForm = new OrderForm(sheetForm);
                model.addAttribute("orderForm", orderForm);
            }
        System.out.println("444444");



        return "orders/orderForm";
    }

    @PostMapping("/orders/new")
    //주문 생성 요청
    public String order(@ModelAttribute OrderForm form, Principal principal) {


        return "redirect:/";
    }

}
