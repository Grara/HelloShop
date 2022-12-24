package pofol.shop.form.create;

import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.dto.business.OrderItemDto;
import pofol.shop.form.UserNameRequiredForm;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 생성 시 필요한 데이터 폼 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-30
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-30
 */
@Data
@NoArgsConstructor
public class CreateOrderForm extends UserNameRequiredForm {
    @NotEmpty(message = "받는분 성함은 필수입니다")
    private String receiverName; //받는 이
    @NotEmpty(message = "연락처는 필수입니다")
    private String receiverPhoneNumber; //받는 이 연락처
    @NotEmpty(message = "기본주소는 필수입니다")
    private String address1; //기본주소
    @NotEmpty(message = "상세주소는 필수입니다")
    private String address2; //상세주소
    private int zipcode; //우편번호
    private String memo; //배송메모
    private List<OrderItemDto> orderItems = new ArrayList<>(); //주문아이템 DTO 리스트
    private int totalPrice; //주문 총 가격
    private Long sheetId; //주문 시트 번호

    public CreateOrderForm(List<OrderItemDto> dtos) {
        dtos.forEach(i -> this.addOrderItem(i));
    }

    public void addOrderItem(OrderItemDto orderItem) {
        //orderItems에 주문아이템DTO 추가하고 총 주문금액에
        orderItems.add(orderItem);
        this.totalPrice += orderItem.getPrice() * orderItem.getCount();
    }
}
