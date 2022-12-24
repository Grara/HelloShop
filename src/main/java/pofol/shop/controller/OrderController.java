package pofol.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pofol.shop.domain.enums.Role;
import pofol.shop.dto.business.OrderDto;
import pofol.shop.dto.business.OrderSearchCondition;
import pofol.shop.dto.security.UserAdapter;
import pofol.shop.exception.NotEnoughQuantityException;
import pofol.shop.form.create.CreateOrderForm;
import pofol.shop.domain.*;
import pofol.shop.domain.embedded.Address;
import pofol.shop.form.create.CreateOrderSheetForm;
import pofol.shop.repository.MemberRepository;
import pofol.shop.repository.OrderItemRepository;
import pofol.shop.repository.OrderRepository;
import pofol.shop.repository.OrderSheetRepository;
import pofol.shop.service.business.MemberService;
import pofol.shop.service.business.OrderService;
import pofol.shop.service.UtilService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 주문과 관련된 뷰를 반환하는 Controller입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-14
 */
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final OrderSheetRepository orderSheetRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final OrderItemRepository orderItemRepository;
    private final UtilService utilService;
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 전체 주문목록 리스트 화면을 반환합니다.
     *
     * @param condition 검색 조건
     * @param pageable  페이징 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-14
     */
    @GetMapping("/orders") //전체 주문 목록, 어드민만 접근 가능
    public String list(@ModelAttribute OrderSearchCondition condition, Model model, Pageable pageable) {

        //Form에서 전달받은 시작날짜 문자열을 LocalDateTime으로 변환, 시간은 00:00으로
        if (StringUtils.hasText(condition.getStartDateInput())) {
            LocalDate temp = LocalDate.parse(condition.getStartDateInput());
            condition.setStartDate(temp.atStartOfDay());
        }

        //Form에서 전달받은 종료날짜 문자열을 LocalDateTime으로 변환, 시간은 23:59으로
        if (StringUtils.hasText(condition.getEndDateInput())) {
            LocalDate temp = LocalDate.parse(condition.getEndDateInput());
            condition.setEndDate(temp.atTime(23, 59));
        }

        //페이징 정보와 주문DTO목록을 받아옴
        Page<OrderDto> results = orderRepository.searchWithPage(condition, pageable);
        utilService.pagingCommonTask(results, model);

        model.addAttribute("search", condition);
        model.addAttribute("orders", results.getContent());
        return "orders/orderList";
    }

    /**
     * 주문을 생성하는 주문시트 화면을 반환합니다.
     *
     * @param id        주문시트의 id
     * @param principal 현재 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-30
     */
    @GetMapping("/orderSheet/{id}")//주문 생성폼 화면
    public String createForm(@PathVariable("id") Long id,
                             Model model,
                             @AuthenticationPrincipal UserAdapter principal) throws Exception {

        //id에 해당하는 주문시트를 가져옴
        OrderSheet sheet = orderSheetRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("id에 해당하는 주문시트가 존재하지않습니다. 입력한 id : " + id));
        Member findMember = sheet.getMember();

        //해당 시트로 이미 주문을 했거나 현재 접근한 Member가 주문시트를 생성한 Member가 아니면 홈화면으로 이동
        if (sheet.getIsOrdered() || !findMember.getUserName().equals(principal.getName())) {
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

    /**
     * 주문 생성 요청을 처리한 후 뷰를 반환합니다.
     *
     * @param form      주문 생성에 필요한 데이터 폼
     * @param principal 현재 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-19
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-13
     */
    @PostMapping("/orders/new") //인가때문에 /orders 사용안하고 이걸로 사용함
    public String order(@Valid CreateOrderForm form, BindingResult result, @AuthenticationPrincipal UserAdapter principal) {

        //Form에 입력한 값에 문제가 있다면 다시 폼 화면으로
        if (result.hasErrors()) {
            return "orders/orderForm";
        }

        OrderSheet sheet;

        try { //이미 주문한 시트거나 존재하지않는 시트라면 홈 화면으로 이동
            sheet = orderSheetRepository.findById(form.getSheetId())
                    .orElseThrow(() -> new NoSuchElementException("주문시트가 존재하지않습니다. 입력한 id : " + form.getSheetId()));
            if (sheet.getIsOrdered()) {
                return "errors/alreadyOrder";
            }
        } catch (NoSuchElementException e) {
            return "errors/alreadyOrder";
        }

        try { //주문 생성
            Address address = new Address(form.getAddress1(), form.getAddress2(), form.getZipcode());
            Delivery delivery = Delivery.builder()
                    .receiverName(form.getReceiverName())
                    .receiverPhoneNumber(form.getReceiverPhoneNumber())
                    .memo(form.getMemo())
                    .address(address)
                    .build();

            Member member = memberService.findByUserName(principal.getName());
            if (sheet.getMember() != member) {
                return "redirect:/";
            }
            orderService.order(member, delivery, form.getOrderItems());
            sheet.setIsOrdered(true);
            orderSheetRepository.save(sheet);
        } catch (NotEnoughQuantityException e) {
            return "errors/quantityError"; //Item 중 재고가 부족한 Item이 있을 경우 안내 페이지로 이동
        } catch (EntityNotFoundException e) {
            return "redirect:/";
        }

        return "errors/alreadyOrder";
    }

    /**
     * 주문의 상세 정보 조회 페이지를 반환합니다.
     *
     * @param id        주문 생성에 필요한 데이터 폼
     * @param principal 현재 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-30
     */
    @GetMapping("/orders/{id}")//주문 상세 정보 조회 화면
    public String orderDetail(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal UserAdapter principal) {
        Order order = orderService.findById(id);
        Member member = memberService.findByUserName(principal.getName());
        if (order.getMember() != member && member.getRole() != Role.ROLE_ADMIN) {
            return "redirect:/";
        }


        List<OrderItem> orderItems = orderItemRepository.findListByOrderFetchItem(order);
        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);
        return "orders/order";
    }

    /**
     * 주문 완료 페이지를 반환합니다.
     *
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-30
     */
    @GetMapping("/orders/cancel-finish")
    public String orderCancelFinish() {
        return "orders/orderCancelFinish";
    }

}
