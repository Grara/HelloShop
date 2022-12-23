package pofol.shop.form.create;

import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.dto.business.OrderItemDto;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateOrderForm {
    @NotEmpty(message = "받는분 성함은 필수입니다")
    private String receiverName;
    @NotEmpty(message = "연락처는 필수입니다")
    private String receiverPhoneNumber;
    @NotEmpty(message = "기본주소는 필수입니다")
    private String address1;
    @NotEmpty(message = "상세주소는 필수입니다")
    private String address2;
    private int zipcode;
    private String memo;
    private List<OrderItemDto> orderItems = new ArrayList<>();
    private int totalPrice;
    private Long sheetId;

    public CreateOrderForm(List<OrderItemDto> dtos) {
        dtos.forEach(i -> this.addOrderItem(i));
    }

    public void addOrderItem(OrderItemDto orderItem) {
        //orderItems에 주문아이템DTO 추가하고 총 주문금액에
        orderItems.add(orderItem);
        this.totalPrice += orderItem.getPrice() * orderItem.getCount();
    }
}
