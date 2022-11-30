package pofol.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pofol.shop.domain.enums.Role;
import pofol.shop.dto.OrderSearchCondition;
import pofol.shop.exception.NotEnoughQuantityException;
import pofol.shop.form.create.CreateOrderForm;
import pofol.shop.domain.*;
import pofol.shop.domain.embedded.Address;
import pofol.shop.form.create.CreateOrderSheetForm;
import pofol.shop.repository.MemberRepository;
import pofol.shop.repository.OrderItemRepository;
import pofol.shop.repository.OrderRepository;
import pofol.shop.repository.OrderSheetRepository;
import pofol.shop.service.CartService;
import pofol.shop.service.ItemService;
import pofol.shop.service.MemberService;
import pofol.shop.service.OrderService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final OrderSheetRepository orderSheetRepository;
    private final MemberRepository memberRepository;
    private final OrderItemRepository orderItemRepository;
    private ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/orders") //전체 주문 목록, 어드민만 접근 가능
    public String orderList(@ModelAttribute OrderSearchCondition condition, Model model){


        if(StringUtils.hasText(condition.getStartDateInput())) {
            LocalDate temp = LocalDate.parse(condition.getStartDateInput());
            condition.setStartDate(temp.atStartOfDay());
        }
        if(StringUtils.hasText(condition.getEndDateInput())) {
            LocalDate temp = LocalDate.parse(condition.getEndDateInput());
            condition.setEndDate(temp.atTime(23,59));
        }

        List<Order> orders = orderRepository.search(condition); //검색 조건으로 주문 목록을 찾음
        model.addAttribute("orderSearch", condition);
        model.addAttribute("orders", orders);
        return "/orders/orderList";
    }

    @GetMapping("/orderSheet/{id}")//주문 생성폼 화면
    public String createForm(@PathVariable("id") Long id,
                             Model model,
                             Principal principal)throws Exception{
        
        //id에 해당하는 주문시트를 가져옴
        OrderSheet sheet = orderSheetRepository.findById(id).orElseThrow();
        Member findMember = sheet.getMember();
        
        //해당 시트로 이미 주문을 했거나 현재 접근한 Member가 주문시트를 생성한 Member가 아니면 홈화면으로 이동
        if (sheet.getIsOrdered() || !findMember.getUserName().equals(principal.getName())){
            return "redirect:/";
        }
        
        //JSON으로 변환한 시트생성 시의 Form내용을 다시 객체로 변환해서 가져옴
        CreateOrderSheetForm content = mapper.readValue(sheet.getContent(), CreateOrderSheetForm.class);
        
        //주문시트의 아이템목록을 인자로 하여 주문 생성 폼을 만듬
        CreateOrderForm orderForm = new CreateOrderForm(content.getItems());
        orderForm.setSheetId(sheet.getId());
        model.addAttribute("createOrderForm", orderForm);

        return "orders/orderForm";
    }

    @PostMapping("/orders/new")//주문 생성 요청
    public String order(@Valid CreateOrderForm form, BindingResult result, Principal principal){

        //Form에 입력한 값에 문제가 있다면 다시 폼 화면으로
        if(result.hasErrors()) {
            return "orders/orderForm";
        }
        
        OrderSheet sheet;
        
        try { //이미 주문한 시트거나 존재하지않는 시트라면 홈 화면으로 이동
            sheet = orderSheetRepository.findById(form.getSheetId()).orElseThrow();
            if(sheet.getIsOrdered()){
                return "errors/alreadyOrder";
            }
        }catch(EntityNotFoundException e) {
            return "errors/alreadyOrder";
        }
        
        try { //주문 생성
            Address address = new Address(form.getAddress1(), form.getAddress2(), form.getZipcode());
            Delivery delivery = new Delivery(address);
            delivery.setReceiverName(form.getReceiverName());
            delivery.setReceiverPhoneNumber(form.getReceiverPhoneNumber());
            delivery.setMemo(form.getMemo());

            Member member = memberRepository.findByUserName(principal.getName()).orElseThrow();
            if(sheet.getMember() != member) {
                return "redirect:/";
            }
            orderService.order(member, delivery, form.getOrderItems());
            sheet.setIsOrdered(true);
            orderSheetRepository.save(sheet);
        }catch(NotEnoughQuantityException e){
            return "errors/quantityError"; //Item 중 재고가 부족한 Item이 있을 경우 안내 페이지로 이동
        }catch(EntityNotFoundException e){
            return "redirect:/";
        }

        return "errors/alreadyOrder";
    }

    @GetMapping("/orders/{id}")//주문 상세 정보 조회 화면
    public String orderDetail(@PathVariable("id")Long id, Model model, Principal principal){
        Order order = orderRepository.findById(id).orElseThrow();
        Member member = memberRepository.findByUserName(principal.getName()).orElseThrow();
        if(order.getMember() != member && member.getRole() != Role.ROLE_ADMIN){
            return "redirect:/";
        }


        List<OrderItem> orderItems = orderItemRepository.findListByOrderFetchItem(order);
        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);
        return "orders/order";
    }

    @GetMapping("/orders/cancel-finish")
    public String orderCancelFinish(){
        return "orders/orderCancelFinish";
    }

}
