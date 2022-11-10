package pofol.shop.formAndDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderSheetForm {
    private String userName;
    private String orderType;
    private List<OrderItemDto> items;

}
