package pofol.shop.form.create;

import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.dto.OrderItemDto;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateOrderSheetForm {
    private String userName;
    private List<OrderItemDto> items;
}
