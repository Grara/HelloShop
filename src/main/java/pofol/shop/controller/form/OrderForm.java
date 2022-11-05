package pofol.shop.controller.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderForm {
    private String orderType;
    @NotEmpty(message = "받는분 성함은 필수입니다")
    private String realName;
    @NotEmpty(message = "기본주소는 필수입니다")
    private String address1;
    @NotEmpty(message = "상세주소는 필수입니다")
    private String address2;
    private int zipcode;
    private List<OrderItemDto> orderItems = new ArrayList<>();
    private int totalPrice;
    private Long itemId;
    private int count;

    public void addOrderItem(OrderItemDto orderItem){
        //orderItems에 주문아이템DTO 추가하고 총 주문금액에
        orderItems.add(orderItem);
        this.totalPrice += orderItem.getPrice() * orderItem.getCount();
    }
}
