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

    @GetMapping("/orderSheet/{id}")
    //주문 생성폼 화면
    public String createForm(@PathVariable("id") Long id,
                             Model model,
                             Principal principal) throws Exception {

        Optional<OrderSheet> opSheet = orderService.findSheetById(id);

        if (opSheet.isPresent()) {
            OrderSheet sheet = opSheet.get();

            if (sheet.getIsOrdered()) return "redirect:/";

            OrderSheetForm sheetForm = mapper.readValue(sheet.getContent(), OrderSheetForm.class);

            if (!sheetForm.getUserName().equals(principal.getName())) {
                return "redirect:/";
            }

            OrderForm orderForm = new OrderForm(sheetForm);
            orderForm.setSheetId(sheet.getId());
            model.addAttribute("orderForm", orderForm);
        }

        return "orders/orderForm";
    }

    @PostMapping("/orders/new")
    //주문 생성 요청
    public String order(@ModelAttribute OrderForm form, Principal principal) {
        Address address = new Address(form.getAddress1(), form.getAddress2(), form.getZipcode());
        Member member = memberService.findOneByName(principal.getName());

        if (form.getOrderType().equals("buy")) {
            Item item = itemService.findOne(form.getOrderItems().get(0).getItemId());
            OrderItem orderItem = new OrderItem(item, form.getOrderItems().get(0).getCount());
            orderService.order(member, address, orderItem);
        }

        if (form.getOrderType().equals("cart")) {
            orderService.orderByCart(member, address, form.getOrderItems());
        }
        OrderSheet sheet = orderService.findSheetById(form.getSheetId()).get();
        sheet.setIsOrdered(true);
        orderService.saveSheet(sheet);

        return "redirect:/";
    }
}
