package pofol.shop.form.create;

import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.dto.business.OrderItemDto;

import java.util.List;

/**
 * 주문시트 생성 시 필요한 데이터 폼 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-10
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-10
 */
@Data
@NoArgsConstructor
public class CreateOrderSheetForm {
    private String userName; //회원명
    private List<OrderItemDto> items; //주문아이템DTO
}
